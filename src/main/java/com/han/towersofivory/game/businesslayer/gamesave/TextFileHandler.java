package com.han.towersofivory.game.businesslayer.gamesave;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;

/**
 * Handles text file operations such as reading, writing, and creating files.
 */
public class TextFileHandler {

    private static final Logger LOGGER = LogManager.getLogger(TextFileHandler.class);

    /**
     * Adds a value to the specified file. If the file is empty, it will recreate the file before adding the value.
     *
     * @param directoryPath the directory path where the file is located
     * @param fileName the name of the file
     * @param value the value to be added to the file
     */
    public void addValueToFile(String directoryPath, String fileName, String value) {
        File file = new File(directoryPath, fileName);
        try {
            if (!checkValueExists(directoryPath, fileName)) {
                createFile(directoryPath, fileName);
            }

            boolean isEmpty = false;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String firstLine = reader.readLine();
                isEmpty = (firstLine == null || firstLine.trim().isEmpty());
                LOGGER.info("First line read from file: {}", firstLine);
            }

            if (isEmpty) {
                LOGGER.info("File is empty, recreating it before writing the value: {}", value);
                Files.delete(file.toPath());
                createFile(directoryPath, fileName);
            }

            // Write the value to the file
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(value);
                LOGGER.info("Successfully wrote value to file: {}", value);
            }
        } catch (IOException e) {
            LOGGER.error("An error occurred while writing to the file: {}", file.getName(), e);
        }
    }

    /**
     * Writes a value to the specified file. If the file does not exist, it will be created before writing the value.
     * If the file already exists, it will be overwritten with the new value.
     *
     * @param directoryPath the directory path where the file is located
     * @param fileName the name of the file
     * @param value the value to be written to the file
     */
    public void writeValueToFile(String directoryPath, String fileName, String value) {
        File file = new File(directoryPath, fileName);
        try {
            if (!checkValueExists(directoryPath, fileName)) {
                createFile(directoryPath, fileName);
            }

            // Write the value to the file
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(value);
                LOGGER.info("Successfully wrote value to file: {}", value);
            }
        } catch (IOException e) {
            LOGGER.error("An error occurred while writing to the file: {}", file.getName(), e);
        }
    }

    /**
     * Creates a file in the specified directory with the given name.
     *
     * @param directoryPath the directory path where the file will be created
     * @param fileName the name of the file to be created
     */
    public void createFile(String directoryPath, String fileName) {
        try {
            File file = new File(directoryPath, fileName);
            if (file.createNewFile()) {
                LOGGER.info("File created: {}", file.getName());
            } else {
                LOGGER.warn("File already exists: {}", file.getName());
            }
        } catch (IOException e) {
            LOGGER.error("An error occurred while creating the file: {}", fileName, e);
        }
    }

    /**
     * Checks if a file exists at the specified directory and file name.
     *
     * @param directoryPath the directory path where the file is located
     * @param fileName the name of the file
     * @return true if the file exists, false otherwise
     */
    public boolean checkValueExists(String directoryPath, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(directoryPath, fileName)))) {
            String firstLine = reader.readLine();
            LOGGER.info("First line read from file: {}", firstLine);
            return firstLine != null;
        } catch (IOException e) {
            LOGGER.error("Error checking if file exists: {} DirectoryPath: {}", fileName, directoryPath, e);
            return false;
        }
    }

    /**
     * Reads the first line of a file from the specified directory and file name.
     *
     * @param directoryPath the directory path where the file is located
     * @param fileName the name of the file
     * @return the first line of the file, or an empty string if an error occurs
     */
    public String readValueFromFile(String directoryPath, String fileName) {
        String value = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(directoryPath, fileName)))) {
            value = reader.readLine();
            LOGGER.info("Successfully read value from file: {}", value);
        } catch (IOException e) {
            LOGGER.error("Error reading file: {} DirectoryPath: {}", fileName, directoryPath, e);
        }
        return value;
    }
}
