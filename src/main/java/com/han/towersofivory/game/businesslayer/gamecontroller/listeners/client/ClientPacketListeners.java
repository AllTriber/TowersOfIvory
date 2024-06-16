package com.han.towersofivory.game.businesslayer.gamecontroller.listeners.client;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.gamecontroller.GameController;
import com.han.towersofivory.game.businesslayer.packets.ActionPacket;
import com.han.towersofivory.game.businesslayer.packets.AgentConfigurationPacket;
import com.han.towersofivory.game.businesslayer.packets.GameStatePacket;
import com.han.towersofivory.game.businesslayer.worldgeneration.generators.FloorGenerator;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.interfaces.IGame;
import com.han.towersofivory.network.businesslayer.packet.IPacketHandler;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.businesslayer.packet.PacketPriority;
import com.han.towersofivory.network.businesslayer.packets.ConnectionPacket;
import com.han.towersofivory.network.businesslayer.packets.DisconnectPacket;
import com.han.towersofivory.network.interfacelayer.interfaces.IClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Class that listens to incoming packets which are sent by the host.
 */
public class ClientPacketListeners implements IPacketListener {
    private static final Logger LOGGER = LogManager.getLogger(ClientPacketListeners.class);
    private final IGame game;
    private final GameController gameController;
    private final IClient network;
    private UUID connectionUUID;

    public ClientPacketListeners(IGame game, GameController gameController, IClient network) {
        this.game = game;
        this.gameController = gameController;
        this.network = network;
    }

    @IPacketHandler
    public void packetHandler(ActionPacket actionPacket) throws IOException {
        LOGGER.info("Client received action packet from host");
        gameController.handleActionOnPeer(actionPacket.getPlayerUUID(), actionPacket.getAction(), actionPacket.getArgs());
    }

    /*
     * UC4 Deelnemen Spel
     *
     * Receives a packet to verify connection and.
     * If own player already set, other player will be set in the world. This means you are the host.
     * Else connectionUUID is set to set the own player when gamestate is received.
     *
     * @param connectionPacket - The packet with connection data
     * @return void
     */
    @IPacketHandler
    public void onConnection(ConnectionPacket connectionPacket) {
        LOGGER.info("Client connection packet received");

        if(game.getWorld().getMyPlayer() != null) {
            gameController.handleJoin(connectionPacket.getConnectionUuid());
        }
        else {
            this.connectionUUID = connectionPacket.getConnectionUuid();
        }
    }

    @IPacketHandler(priority = PacketPriority.LOWEST)
    public void sendAgentConfigToHost(ConnectionPacket connectionPacket) {
        LOGGER.info("sending agent config to host");
        AgentConfigurationPacket agentConfigurationPacket = new AgentConfigurationPacket(game.getWorld().getMyAgentConfiguration(), connectionPacket.getUuid());
        agentConfigurationPacket.setTargetUUID(connectionPacket.getUuid());
        network.sendPacketToHost(agentConfigurationPacket);
    }


    @IPacketHandler
    public void packetHandler(AgentConfigurationPacket agentConfigurationPacket) {
        LOGGER.info("Client received agent configuration packet. this agentConfig: {}", agentConfigurationPacket.getAgentConfig().getConfiguration());

        UUID targetUUID = agentConfigurationPacket.getTargetUUID();
        List<AgentConfigurationText> agents = game.getWorld().getAgents();

        boolean exists = agents.stream()
                .anyMatch(agent -> agent.getTitle().equals(targetUUID.toString()));

        if (!exists) {
            agents.add(new AgentConfigurationText(targetUUID.toString(), agentConfigurationPacket.getAgentConfig().getConfiguration()));

            LOGGER.info("Added new agent configuration for UUID: {}", targetUUID);
        } else {
            LOGGER.info("Agent configuration for UUID: {} already exists, not adding.", targetUUID);
        }

        agents.forEach(agent -> LOGGER.info("Agent Title: {}, Configuration: {}", agent.getTitle(), agent.getConfiguration()));
    }

    
    @IPacketHandler
    public void onDisconnect(DisconnectPacket disconnectPacket) {
        LOGGER.info("Client disconnection packet received");
        gameController.handleLeave(disconnectPacket.getConnectionUuid());
        network.sendPacketToHost(disconnectPacket);
    }

    /*
     * UC4 Deelnemen Spel
     *
     * Receives a packet with the gamestate.
     * Then the gamestate is set in Game component.
     * All players are set in de world.
     *q
     * @param gameStatePacket - The packet with gamestate data
     * @return void
     */
    @IPacketHandler
    public void onGameStatePacket(GameStatePacket gameStatePacket) {
        LOGGER.info("Gamestate packet received");

        game.setGameState(gameStatePacket.getGameState());
        game.handleJoin(connectionUUID);
        game.updatePlayers(gameStatePacket);
        game.handleLowestFloorLevelUpdate(gameStatePacket);

        GameState gameState = gameStatePacket.getGameState();

        for (var player : gameState.getPlayers()) {
            if (player.getUUID() != gameController.getWorld().getMyPlayer().getUUID()) {
                gameController.getWorld().setOtherPlayer(player);
            }
        }

        World world = game.getWorld();
        FloorGenerator floorGenerator = game.getGameWorldService().getFloorGenerator();


        if (gameState.getLowestFloorLevel() > world.getFloors().getFirst().getLevel()) {
            setPlayerOnLowestFloor(gameState, world, floorGenerator);
        }

        for(Position pickedUpItemPosition : gameState.getPickedUpItemsPositions()){
            Floor floor = world.getFloor(pickedUpItemPosition.getZ());
            if(floor == null) {
                floor = floorGenerator.generateFloor(gameState.getGameInfo().getGameConfiguration().getSeed() + pickedUpItemPosition.getZ(), pickedUpItemPosition.getZ());
                world.addFloor(floor);
            }
            floor.removeItemFromRoom(pickedUpItemPosition);
        }

        game.getGameWorldService().updateWorld();
    }

    private void setPlayerOnLowestFloor(GameState gameState, World world, FloorGenerator floorGenerator){
        List<Floor> floors = new ArrayList<>();
        floors.add(floorGenerator.generateFloor(gameState.getGameInfo().getGameConfiguration().getSeed() + gameState.getLowestFloorLevel(), gameState.getLowestFloorLevel()));
        world.setFloors(floors);
        world.getMyPlayer().getPosition().setZ(gameState.getLowestFloorLevel());
        world.spawnPlayerOnWalkableTile(world.getMyPlayer(), gameState.getLowestFloorLevel());
        game.getGameWorldService().updateWorld();
    }


    public UUID getConnectionUUID() {
        return connectionUUID;
    }
}

