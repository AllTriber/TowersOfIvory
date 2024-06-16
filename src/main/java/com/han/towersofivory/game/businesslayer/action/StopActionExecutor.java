package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/*
    * This class is responsible for stopping the game.
 */
public class StopActionExecutor extends ActionBase{
    private static final Logger LOGGER = LogManager.getLogger(StopActionExecutor.class);
    private GameMapper gameMapper;


    public StopActionExecutor(GameMapper gameMapper) {
        super("stopGame", null, "Stops the game");
        this.gameMapper = gameMapper;
    }

    public void setGameMapper(GameMapper gameMapper) {
        this.gameMapper = gameMapper;
    }

    /**
     * Performs the specified action, which in this case stops the game.
     *
     * @param player the player initiating the action
     * @param world the game world
     * @param cmd the command being executed
     * @param args the arguments passed with the command
     * @throws IOException if an error occurs while saving the game state
     */
    @Override
    public void performAction(Player player, World world, String cmd, String[] args) throws IOException {
        LOGGER.info("Stopping the game (in stopActionExecutor)");

        try {
            gameMapper.saveGameState();
            LOGGER.info("Game state saved successfully.");
        } catch (IOException e) {
            throw new IOException("Failed to save game state", e);
        }

        world.stopGame();
        LOGGER.info("Game stopped successfully.");
    }

    /**
     * Checks if the player is the host of the game, so only the host can stop the game.
     *
     * @param player the player that wants to stop the game
     * @param world the game world
     * @param cmd the command being executed
     * @param args the arguments passed with the command
     * @return true if the player is the host, false otherwise (only the host may stop the game)
     */
    @Override
    public boolean specificCheckAction(Player player, World world, String cmd, String[] args) {
        return player.getUUID() == world.getMyPlayerUUID();
    }
}
