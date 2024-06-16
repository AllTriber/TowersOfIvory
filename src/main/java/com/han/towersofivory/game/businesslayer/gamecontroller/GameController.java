package com.han.towersofivory.game.businesslayer.gamecontroller;

import com.han.towersofivory.agent.interfacelayer.interfaces.IAgent;
import com.han.towersofivory.agent.interfacelayer.mappers.ConcreteAgent;
import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.gamecontroller.listeners.client.ClientPacketListeners;
import com.han.towersofivory.game.businesslayer.gamecontroller.listeners.host.ServerPacketListeners;
import com.han.towersofivory.game.businesslayer.gamesave.GameSaver;
import com.han.towersofivory.game.businesslayer.packets.ActionPacket;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.interfaces.IGame;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.dto.HostInformation;
import com.han.towersofivory.network.interfacelayer.interfaces.IClient;
import com.han.towersofivory.network.interfacelayer.interfaces.IHost;
import com.han.towersofivory.network.interfacelayer.mappers.NetworkMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/*
 * This class is the facade controller for the gameworld.
 */
public class GameController {
    private static final Logger LOGGER = LogManager.getLogger(GameController.class);
    private final IClient client;
    private final IHost host;
    private final IAgent agent;
    private final IGame game;
    private GameState gameState;

    public GameController(IGame game) {
        NetworkMapper networkMapper = new NetworkMapper();
        this.client = networkMapper;
        this.host = networkMapper;
        this.agent = new ConcreteAgent();
        this.game = game;
    }

    public HostInformation createGame(GameInfo gameInfo) throws ConnectionFailedException {
        LOGGER.info("Created game");
        HostInformation hostInformation = host.startHost(gameInfo);
        host.registerHostListener(new ServerPacketListeners(this, host));
        return hostInformation;
    }

    /*
     * UC4 Deelnemen Spel
     *
     * Calls network component to join a game.
     *
     * @param hostInformation - The information about the host
     * @return void
     */
    public void joinGame(HostInformation hostInformation) throws ConnectionFailedException {
        client.registerClientListener(new ClientPacketListeners(game, this, client));
        client.connectToHost(hostInformation);
    }

    public void resumeGame(IGame game) {
        throw new UnsupportedOperationException();
    }

    public void sendInput(String input) {
        if (input == null || input.isEmpty() || game.getWorld() == null || game.getWorld().getMyPlayer() == null) {
            return;
        }

        String[] actionSplitted = input.split("\\s+");

        String action = actionSplitted[0];

        String[] actionArgs = Arrays.copyOfRange(actionSplitted, 1, actionSplitted.length);

        ActionPacket actionPacket = new ActionPacket(game.getWorld().getMyPlayer().getUUID(), action, actionArgs);
        client.sendPacketToHost(actionPacket);
    }

    public List<HostInformation> searchForHosts() {
        return client.findHosts();
    }
   public IAgent getAgent() {
       return this.agent;
    }

   /*
    * UC2 Spelen Spel
    *
    * Handles an action on the side of the host,
    * by first performing the checks and then sending the new gamestate to all peers in the game.
    *
    * @param player - The player performing the action
    * @param command - the input command the player entered into the TUI.
    * @param args - the optional arguments for the command the player entered into the TUI.
    */
    public void handleActionOnHost(UUID player, String command, String[] args) {
        LOGGER.info("Handling action on host");
        if (game.checkAction(player, command, args)) {
            host.sendPacketToClients(new ActionPacket(player, command, args));
        }
    }

    /*
     * UC2 Spelen Spel
     *
     * Handles an action on the side of the peer, after receiving the action from the host.
     *
     * @param player - The player performing the action
     * @param command - the input command the player entered into the TUI.
     * @param args - the optional arguments for the command the player entered into the TUI.
     */
    public void handleActionOnPeer(UUID player, String command, String[] args) throws IOException {
        game.performAction(player, command, args);
    }

    /*
     * UC1 Starten Spel
     *
     * Handles the game configuration, by setting the configuration in the game.
     *
     * @param gameConfiguration - The configuration of the game.
     */
    public void handleJoin(UUID uuid) {
        LOGGER.info("Handling connection");
        game.handleJoin(uuid);
    }

    /*
     * UC1 Starten Spel
     *
     * Handles the game configuration, by setting the configuration in the game.
     *
     * @param gameConfiguration - The configuration of the game.
     */
    public void handleLeave(UUID uuid) {
        LOGGER.info("Handling connection");
        game.handleLeave(uuid);
    }

    public GameConfiguration getGameConfiguration() {
        return game.getGameConfiguration();
    }

    public World getWorld() {
        return game.getWorld();
    }

    /*
     * UC1 Starten Spel
     *
     * Before returning the gameState, it sets the lowest floor level and
     * adds a list with the positions of all the items that have been picked up
     *
     * @return gameState
     */
    public void updateGameStateBeforeJoin(){
        gameState.setLowestFloorLevel(game.getWorld().getFloors().getFirst().getLevel());

        List<Position> pickedUpItemsPositions = new ArrayList<>();
        for(Floor floor : game.getWorld().getFloors()){
            pickedUpItemsPositions.addAll(floor.getPickedUpItemsPositions());
        }

        gameState.setPickedUpItemsPositions(pickedUpItemsPositions);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;

    }

    public void stopGame() {
        //not needed for current implementation of stop game (which is via system.exit, instead of back to main menu)
    }

    public void saveGameState() throws IOException {
        GameSaver.saveGameState(getGameState());
    }
}
