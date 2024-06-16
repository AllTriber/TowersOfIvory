package com.han.towersofivory.game.businesslayer.gamesave;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerUUIDHandlerTest {

    private PlayerUUIDHandler SUT;
    private static final String TEST_DIRECTORY_PATH = "src/test/resources";
    private static final String FILE_NAME = "playerID.txt";
    private static final Path FILE_PATH = Path.of(TEST_DIRECTORY_PATH, FILE_NAME);

    @BeforeEach
    void setUp() throws IOException {
        SUT = new PlayerUUIDHandler(TEST_DIRECTORY_PATH);
        // Ensure the directory exists
        Files.createDirectories(Path.of(TEST_DIRECTORY_PATH));
        // Delete the file if it exists to start fresh
        if (Files.exists(FILE_PATH)) {
            Files.delete(FILE_PATH);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the file after each test
        if (Files.exists(FILE_PATH)) {
            Files.delete(FILE_PATH);
        }
    }

    @AfterAll
    static void cleanUp() throws IOException {
        // Delete the directory if it's empty
        Files.deleteIfExists(Path.of(TEST_DIRECTORY_PATH));
    }

    @Test
    void testCheckMyPlayerIdExists_FileDoesNotExist() {
        // Act
        SUT.checkMyPlayerIdExists();

        // Assert
        assertFalse(Files.exists(FILE_PATH), "File should not be created");
    }

    @Test
    void testCheckMyPlayerIdExists_FileAlreadyExists_DoesNotCreateNewFile() throws IOException {
        // Arrange
        UUID originalUUID = UUID.randomUUID();
        Files.writeString(FILE_PATH, originalUUID.toString());

        // Act
        SUT.checkMyPlayerIdExists();

        // Assert
        assertTrue(Files.exists(FILE_PATH), "File should exist");
        String currentUUID = Files.readString(FILE_PATH);
        assertEquals(originalUUID.toString(), currentUUID, "File content should not change");
    }

    @Test
    void testReadPlayerIdFromFile_FileExists_ReturnsCorrectUUID() throws IOException {
        // Arrange
        UUID expectedUUID = UUID.randomUUID();
        Files.writeString(FILE_PATH, expectedUUID.toString());

        // Act
        UUID actualUUID = SUT.readPlayerIdFromFile();

        // Assert
        assertEquals(expectedUUID, actualUUID, "Returned UUID should match the expected UUID");
    }

    @Test
    void testReadPlayerIdFromFile_FileDoesNotExist_ThrowsException() {
        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SUT.readPlayerIdFromFile();
        });

        // Assert
        assertTrue(exception.getMessage().contains("Invalid UUID string"), "Exception should be thrown for invalid UUID string");
    }
}
