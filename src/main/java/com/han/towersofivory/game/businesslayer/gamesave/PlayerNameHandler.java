package com.han.towersofivory.game.businesslayer.gamesave;

/**
 * The player name handler
 */
public class PlayerNameHandler {

    private String directoryPath = "src/main/resources";
    private static final String FILE_NAME = "playerName.txt";
    private final TextFileHandler textFileHandler = new TextFileHandler();

    public PlayerNameHandler() {
    }
    public PlayerNameHandler(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    /**
     * Check if the player name exists
     * @return boolean - True if the player name exists, false if it does not
     */
    public boolean checkMyPlayerNameExists() {
        return textFileHandler.checkValueExists(directoryPath, FILE_NAME);
    }

    /**
     * Read the player name from the file
     * @return String - The player name
     */
    public String readPlayerNameFromFile() {
        return textFileHandler.readValueFromFile(directoryPath, FILE_NAME);
    }

    /**
     * Write the player name to the file
     * @param playerName - The player name to write
     */
    public void writePlayerNameToFile(String playerName) {
        textFileHandler.writeValueToFile(directoryPath, FILE_NAME, playerName);
    }
}
