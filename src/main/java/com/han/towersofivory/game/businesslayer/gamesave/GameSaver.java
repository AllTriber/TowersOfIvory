package com.han.towersofivory.game.businesslayer.gamesave;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class GameSaver {
    private static final String DIRECTORY = "src/main/resources/saveGames";
    private static final Logger LOGGER = LogManager.getLogger(GameSaver.class);

    private GameSaver(){}

    // Save the game state to a file
    public static void saveGameState(GameState gameState) throws IOException {
        String fileName = gameState.getGameName();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY + "/" + fileName + ".txt"))) {
            oos.writeObject(gameState);
        }
    }

    // Load the game state from a file
    public static GameState loadGameState(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY + "/" + fileName + ".txt"))) {
            return (GameState) ois.readObject();
        }
    }

    /**
     * UC9: Hervatten spel
     * This method loads all saved game state files from the saveGames folder.
     *
     * @return File[] - An array of files
     */
    public static File[] getAllSavedGames() {
        File directory = new File(DIRECTORY);

        // Retrieve a list of files in the folder
        File[] files = directory.listFiles();

        // Ensure files array is not null
        if(files == null){
            LOGGER.error("An error occurred while loading configurations. Error: No files found in the directory.");
            return new File[0];
        }
        return files;
    }
}
