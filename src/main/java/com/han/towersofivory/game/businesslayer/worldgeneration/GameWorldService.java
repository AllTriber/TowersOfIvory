package com.han.towersofivory.game.businesslayer.worldgeneration;

import com.han.towersofivory.agent.interfacelayer.mappers.ConcreteAgent;
import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.action.*;
import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.packets.ActionPacket;
import com.han.towersofivory.game.businesslayer.worldgeneration.generators.FloorGenerator;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.ui.interfacelayer.mappers.UIMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/*
 * This class is the facade controller of the gameworld/gamestate.
 */
public class GameWorldService {
    private static final Logger LOGGER = LogManager.getLogger(GameWorldService.class);
    private final FloorGenerator floorGenerator;
    private final ActionDispatcher actionManager;
    private GameConfiguration gameConfiguration;
    private World world;
    private UIMapper ui;
    private GameState gameState;
    private ConcreteAgent agent;

    public GameWorldService(UIMapper ui, GameMapper gameMapper) {
        this.ui = ui;
        this.floorGenerator = new FloorGenerator(100, 100, 10, 15);
        this.actionManager = new ActionDispatcher();
        this.actionManager.register(new MoveActionExecutor());
        this.actionManager.register(new InteractionActionExecutor(floorGenerator));
        this.agent = new ConcreteAgent();
        this.actionManager.register(new EliminationActionExecutor());
        this.actionManager.register(new StopActionExecutor(gameMapper));
        this.actionManager.register(new ChatActionExecutor());
    }

    public void updateWorld() {
        LOGGER.info("Updating world");
        ui.updateUI(world, null);
    }

    public void generateWorld(GameConfiguration gameConfiguration) {
        LOGGER.info("Generating world");

        this.world = new World();

        Floor floor = floorGenerator.generateFloor(gameConfiguration.getSeed(), 0);
        world.addFloor(floor);
    }

    public void configureGame(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    private Player getPlayerByUUID(UUID uuid) {
        if (world == null) {
            return null;
        }

        if (world.getMyPlayer() == null) {
            return null;
        }

        if (world.getMyPlayer().getUUID().equals(uuid)) {
            return world.getMyPlayer();
        }

        Optional<Player> optionalPlayer = world.getOtherPlayers().stream()
                .filter(player -> player.getUUID().equals(uuid))
                .findFirst();

        return optionalPlayer.orElse(null);
    }

    /**
     * UC2 Spelen Spel
     * <p>
     * Checks an action on the side of the host,
     * The flow of the checks is implemented in the subclass of the ActionBase action.
     *
     * @param command - the input command the player entered into the TUI.
     * @param args    - the optional arguments for the command the player entered into the TUI.
     */
    public boolean checkAction(UUID playerUUID, String command, String[] args) {
        ActionBase action = actionManager.getByAction(command);
        Player player = getPlayerByUUID(playerUUID);
        if (player == null)
            return false;

        return action.checkAction(player, world, command, args);
    }

    /**
     * UC1 Starten Spel
     * <p>
     * Handles the connection of a new player to the game.
     *
     * @param uuid - The UUID of the player that joined the game.
     */
    public void handlePlayerJoin(UUID uuid) {
        LOGGER.info("Handling player join");

        Player player = getPlayerByUUID(uuid);

        if (player == null) {
            if (world.getMyPlayer() == null) {
                handleNewMyPlayer(uuid);
            } else {
                handleNewOtherPlayer(uuid);
            }
        }

        ui.updateUI(world, null);
    }

    /**
     * UC1 Starten Spel
     * <p>
     * Handles the connection of a new player to the game.
     *
     * @param uuid - The UUID of the player that joined the game.
     */
    private void handleNewMyPlayer(UUID uuid) {
        Player myPlayer = new Player(uuid);
        world.setMyPlayer(myPlayer);
        gameState.addPlayer(myPlayer);
        if (!world.spawnPlayerOnWalkableTile(myPlayer, world.getFloors().getFirst().getLevel())) {
            world.setMyPlayer(null);
        }
    }

    /**
     * UC1 Starten Spel
     * <p>
     * Handles the connection of a new player to the game.
     *
     * @param uuid - The UUID of the player that joined the game.
     */
    private void handleNewOtherPlayer(UUID uuid) {
        Player otherPlayer = new Player(uuid);
        world.addOtherPlayer(otherPlayer);
        gameState.addPlayer(otherPlayer);
        if (!world.spawnPlayerOnWalkableTile(otherPlayer, world.getFloors().getFirst().getLevel())) {
            world.removePlayer(otherPlayer);
        }
    }

    /**
     * UC1 Starten Spel
     * <p>
     * Handles the disconnection of a player from the game.
     *
     * @param uuid - The UUID of the player that left the game.
     */
    public void handlePlayerLeave(UUID uuid) {
        LOGGER.info("Handling player leave");

        Player player = getPlayerByUUID(uuid);
        if (player != null) {
            world.removePlayer(player);
        }

        ui.updateUI(world, null);
    }

    /**
     * UC2 Spelen Spel
     * <p>
     * Perfroms an action on the side of the host, and changes the gamestate accordingly.
     * Also sends the action to all peers in the game.
     *
     * @param command - the input command the player entered into the TUI.
     * @param args    - the optional arguments for the command the player entered into the TUI.
     */
    public void performAction(UUID playerUUID, String command, String[] args) throws IOException {
        ActionBase action = actionManager.getByAction(command);
        Player player = getPlayerByUUID(playerUUID);

        if (player == null)
            return;

        action.performAction(player, world, command, args);

        ui.updateUI(world, new ActionPacket(playerUUID, command, args));

        player.updatePlayerVision(world, command);
    }

    public World getWorld() {
        return world;
    }

    public GameConfiguration getGameConfiguration() {
        return gameConfiguration;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * UC1 Starten Spel
     * <p>
     * Generates the seed for the game:
     * If the seed is empty, the seed will be the current time in milliseconds.
     * Else the seed will be the seed that was entered in the TUI.
     *
     * @param gameSeed The text that was entered in the TUI.
     * @return The seed for the game.
     */
    public Long getSeed(String gameSeed) {
        return gameSeed.isEmpty() ? System.currentTimeMillis() : Long.parseLong(gameSeed);
    }

    public FloorGenerator getFloorGenerator() {
        return floorGenerator;
    }
}
