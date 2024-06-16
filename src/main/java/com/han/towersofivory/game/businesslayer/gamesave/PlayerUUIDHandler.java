package com.han.towersofivory.game.businesslayer.gamesave;

import java.util.UUID;

/**
 * The player ID handler
 */
public class PlayerUUIDHandler {

    private String directoryPath = "src/main/resources";
    private static final String FILE_NAME = "playerID.txt";
    private final TextFileHandler textFileHandler = new TextFileHandler();

    public PlayerUUIDHandler() {
    }
    public PlayerUUIDHandler(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    /**
     * Set up the player ID
     * If the player ID does not exist, create a new one
     */
    public void setup() {
        if(!checkMyPlayerIdExists()) {
            writePlayerIdToFile(UUID.randomUUID());
        }
    }

    /**
     * Check if the player ID exists
     * @return boolean - True if the player ID exists, false if it does not
     */
    public boolean checkMyPlayerIdExists() {
        return textFileHandler.checkValueExists(directoryPath, FILE_NAME);
    }

    /**
     * Read the player ID from the file
     * @return UUID - The player ID
     */
    public UUID readPlayerIdFromFile() {
        return UUID.fromString(textFileHandler.readValueFromFile(directoryPath, FILE_NAME));
    }

    /**
     * Write the player ID to the file
     * @param playerId - The player ID to write
     */
    public void writePlayerIdToFile(UUID playerId) {
        textFileHandler.writeValueToFile(directoryPath, FILE_NAME, playerId.toString());
    }
}
