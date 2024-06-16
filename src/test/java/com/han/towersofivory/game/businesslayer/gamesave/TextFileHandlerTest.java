package com.han.towersofivory.game.businesslayer.gamesave;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TextFileHandlerTest {

    private TextFileHandler SUT;
    private String directoryPath;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        SUT = new TextFileHandler();
        directoryPath = tempDir.toString();
    }

    @Test
    void testAddValueToFile_FileDoesNotExist() throws IOException {
        String fileName = "testFile.txt";
        String value = "Hello, World!";

        SUT.addValueToFile(directoryPath, fileName, value);

        Path filePath = Paths.get(directoryPath, fileName);
        assertTrue(Files.exists(filePath));
        assertEquals(value, Files.readString(filePath));
    }

    @Test
    void testAddValueToFile_FileExistsButEmpty() throws IOException {
        String fileName = "testFile.txt";
        String value = "Hello, World!";

        Files.createFile(Paths.get(directoryPath, fileName));

        SUT.addValueToFile(directoryPath, fileName, value);

        Path filePath = Paths.get(directoryPath, fileName);
        assertTrue(Files.exists(filePath));
        assertEquals(value, Files.readString(filePath));
    }

    @Test
    void testAddValueToFile_FileExistsAndNotEmpty() throws IOException {
        String fileName = "testFile.txt";
        String existingValue = "Existing Content\n";
        String newValue = "New Content";

        Files.writeString(Paths.get(directoryPath, fileName), existingValue);

        SUT.addValueToFile(directoryPath, fileName, newValue);

        Path filePath = Paths.get(directoryPath, fileName);
        assertTrue(Files.exists(filePath));
        assertEquals(existingValue + newValue, Files.readString(filePath));
    }

    @Test
    void testWriteValueToFile_FileDoesNotExist() throws IOException {
        String fileName = "testFile.txt";
        String value = "Hello, World!";

        SUT.writeValueToFile(directoryPath, fileName, value);

        Path filePath = Paths.get(directoryPath, fileName);
        assertTrue(Files.exists(filePath));
        assertEquals(value, Files.readString(filePath));
    }

    @Test
    void testWriteValueToFile_FileExists() throws IOException {
        String fileName = "testFile.txt";
        String existingValue = "Existing Content";
        String newValue = "New Content";

        Files.writeString(Paths.get(directoryPath, fileName), existingValue);

        SUT.writeValueToFile(directoryPath, fileName, newValue);

        Path filePath = Paths.get(directoryPath, fileName);
        assertTrue(Files.exists(filePath));
        assertEquals(newValue, Files.readString(filePath));
    }

    @Test
    void testCreateFile_FileDoesNotExist() {
        String fileName = "testFile.txt";

        SUT.createFile(directoryPath, fileName);

        Path filePath = Paths.get(directoryPath, fileName);
        assertTrue(Files.exists(filePath));
    }

    @Test
    void testCreateFile_FileExists() throws IOException {
        String fileName = "testFile.txt";

        Files.createFile(Paths.get(directoryPath, fileName));
        SUT.createFile(directoryPath, fileName);

        Path filePath = Paths.get(directoryPath, fileName);
        assertTrue(Files.exists(filePath)); // File should still exist
    }

    @Test
    void testCheckValueExists_FileDoesNotExist() {
        String fileName = "nonExistentFile.txt";

        assertFalse(SUT.checkValueExists(directoryPath, fileName));
    }

    @Test
    void testCheckValueExists_FileExistsButEmpty() throws IOException {
        String fileName = "emptyFile.txt";

        Files.createFile(Paths.get(directoryPath, fileName));

        assertFalse(SUT.checkValueExists(directoryPath, fileName));
    }

    @Test
    void testCheckValueExists_FileExistsAndNotEmpty() throws IOException {
        String fileName = "testFile.txt";
        String value = "Hello, World!";

        Files.writeString(Paths.get(directoryPath, fileName), value);

        assertTrue(SUT.checkValueExists(directoryPath, fileName));
    }

    @Test
    void testReadValueFromFile_FileDoesNotExist() {
        String fileName = "nonExistentFile.txt";

        assertEquals("", SUT.readValueFromFile(directoryPath, fileName));
    }

    @Test
    void testReadValueFromFile_FileExistsAndNotEmpty() throws IOException {
        String fileName = "testFile.txt";
        String value = "Hello, World!";

        Files.writeString(Paths.get(directoryPath, fileName), value);

        assertEquals(value, SUT.readValueFromFile(directoryPath, fileName));
    }
}
