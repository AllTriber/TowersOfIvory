package com.han.towersofivory.game.interfacelayer.mappers;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.gamecontroller.GameController;
import com.han.towersofivory.game.businesslayer.packets.GameStatePacket;
import com.han.towersofivory.game.businesslayer.worldgeneration.GameWorldService;
import com.han.towersofivory.game.businesslayer.worldgeneration.generators.FloorGenerator;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.interfaces.IGame;
import com.han.towersofivory.game.interfacelayer.interfaces.IGetPlayer;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.dto.HostInformation;
import com.han.towersofivory.ui.interfacelayer.mappers.UIMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameMapper implements IGame, IGetPlayer {
    private static final Logger LOGGER = LogManager.getLogger(GameMapper.class);
    private GameWorldService worldService;
    private GameController gameController;

    public GameMapper(UIMapper ui) {
        this.worldService = new GameWorldService(ui, this);
        this.gameController = new GameController(this);
    }

    @Override
    public boolean checkAction(UUID player, String cmd, String[] args) {
        return worldService.checkAction(player, cmd, args);
    }

    @Override
    public void performAction(UUID player, String cmd, String[] args) throws IOException {
        worldService.performAction(player, cmd, args);
    }

    @Override
    public void handleJoin(UUID uuid) {
        worldService.handlePlayerJoin(uuid);
    }

    @Override
    public void handleLeave(UUID uuid) {
        worldService.handlePlayerLeave(uuid);
    }

    @Override
    public HostInformation createGame(GameInfo gameInfo) throws ConnectionFailedException {
        return gameController.createGame(gameInfo);
    }

    @Override
    public void stopGame() {
        gameController.stopGame();
        //not needed for current implementation of stop game (which is via system.exit, instead of back to main menu)
    }

    @Override
    public void joinGame(HostInformation hostInformation, AgentConfigurationText selectedAgentConfig) throws ConnectionFailedException {
        gameController.joinGame(hostInformation);
        worldService.getWorld().setMyAgentConfiguration(selectedAgentConfig);
    }

    @Override
    public void sendInput(String input) {
        gameController.sendInput(input);
    }

    @Override
    public World getWorld() {
        return worldService.getWorld();
    }

    @Override
    public List<HostInformation> getHosts() {
        return gameController.searchForHosts();
    }

    public void configureGame(GameConfiguration configuration) {
        worldService.configureGame(configuration);
    }

    @Override
    public void generateWorld(GameConfiguration gameConfiguration) {
        worldService.generateWorld(gameConfiguration);
    }

    @Override
    public GameConfiguration getGameConfiguration() {
        return worldService.getGameConfiguration();
    }

    @Override
    public void saveAgentConfiguration(AgentConfigurationText agentConfigurationText) {
        worldService.getWorld().getAgents().add(agentConfigurationText);
    }

    public GameState getGameState() {
        return gameController.getGameState();
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameController.setGameState(gameState);
        this.worldService.setGameState(gameState);
    }

    public Long getSeed(String seed) {
        return worldService.getSeed(seed);
    }

    @Override
    public GameWorldService getGameWorldService() {
        return worldService;
    }

    /**
     * UC 6
     * Retrieves a player with the specified UUID.
     *
     * @param uuid The UUID of the player to retrieve.
     * @return The player with the specified UUID if found, otherwise null.
     */
    @Override
    public Player getplayer(UUID uuid) {
        return worldService.getWorld().getPlayers().stream()
                .filter(player -> player.getUUID().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveGameState() throws IOException {
        gameController.saveGameState();

    }

    @Override
    public void updatePlayers(GameStatePacket gameStatePacket ) {
        UUID myPlayerUUID = gameController.getWorld().getMyPlayer().getUUID();

        for (var player : gameStatePacket.getGameState().getPlayers()) {
            if (!player.getUUID().equals(myPlayerUUID)) {
                boolean playerExists = gameController.getWorld().getOtherPlayers()
                        .stream()
                        .anyMatch(existingPlayer -> existingPlayer.getUUID().equals(player.getUUID()));

                if (!playerExists) {
                    gameController.getWorld().setOtherPlayer(player);
                } else {
                    LOGGER.warn("Duplicate player UUID detected: {}", player.getUUID());
                }
            }
        }
    }
    @Override
    public void handleLowestFloorLevelUpdate(GameStatePacket gameStatePacket) {
        World world = worldService.getWorld();

        if (gameStatePacket.getGameState().getLowestFloorLevel() > world.getFloors().getFirst().getLevel()) {
            FloorGenerator floorGenerator = worldService.getFloorGenerator();
            List<Floor> floors = new ArrayList<>();
            floors.add(floorGenerator.generateFloor(gameStatePacket.getGameState().getGameInfo().getGameConfiguration().getSeed() + gameStatePacket.getGameState().getLowestFloorLevel() , gameStatePacket.getGameState().getLowestFloorLevel()));
            world.setFloors(floors);
            world.getMyPlayer().getPosition().setZ(gameStatePacket.getGameState().getLowestFloorLevel());
            world.spawnPlayerOnWalkableTile(world.getMyPlayer(), gameStatePacket.getGameState().getLowestFloorLevel());
            worldService.updateWorld();
        }
    }
}
