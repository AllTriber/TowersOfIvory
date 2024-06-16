package com.han.towersofivory.agent.businesslayer.services;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.agent.exceptions.AgentCompilerException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


class AgentServiceTest {

    private static final String CONFIGURATIONS_PATH = "src/test/java/resources/";
    private AgentService agentService;
    private final AgentConfigurationText agentConfigurationText = Mockito.mock(AgentConfigurationText.class);

    @BeforeEach
    void setUp() throws IOException {
        // Ensure the directory exists
        Files.createDirectories(Paths.get(CONFIGURATIONS_PATH));
        agentService = new AgentService(CONFIGURATIONS_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up files created during the test
        Files.walk(Paths.get(CONFIGURATIONS_PATH))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @AfterAll
    static void tearDownAll() throws IOException {
        // Clean up any remaining files and directories
        Files.walk(Paths.get(CONFIGURATIONS_PATH))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void saveConfigurationTest() {
        // Arrange
        String testFileName = "saveConfigFileTest";
        String filePath = CONFIGURATIONS_PATH + testFileName + ".txt";
        Path path = Paths.get(filePath);

        when(agentConfigurationText.getTitle()).thenReturn(testFileName);
        when(agentConfigurationText.getConfiguration()).thenReturn("configuration");

        // Mock Files.write
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            byte[] expectedContent = "configuration".getBytes();
            mockedFiles.when(() -> Files.write(path, expectedContent)).thenReturn(path);

            // Act
            agentService.saveConfiguration(agentConfigurationText);

            // Assert
            mockedFiles.verify(() -> Files.write(path, expectedContent));
        }
    }

    @Test
    void saveConfigurationTest_IOException() throws IOException {
        // Arrange
        String testFileName = "saveConfigFileTest";
        String filePath = CONFIGURATIONS_PATH + testFileName + ".txt";
        Path path = Paths.get(filePath);

        when(agentConfigurationText.getTitle()).thenReturn(testFileName);
        when(agentConfigurationText.getConfiguration()).thenReturn("configuration");

        // Mock Files.write to throw an IOException
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            when(Files.write(path, "configuration".getBytes())).thenThrow(new IOException("File system error"));

            // Act
            agentService.saveConfiguration(agentConfigurationText);

            // Assert
            mockedFiles.verify(() -> Files.write(path, "configuration".getBytes()));
        }
    }

    @Test
    void testGenerateFileThrowsIllegalArgumentExceptionWhenIncorrectSyntax() {
        // Arrange
        AgentConfigurationText mockAgentConfigurationText = new AgentConfigurationText("test", "Wanneer mijn levenspunten meer dan 5 zijn, dan ");

        // Act & Assert
        assertThrows(AgentCompilerException.class, () -> agentService.generateFile(mockAgentConfigurationText),
                "SEVERE: Parse error: [Syntax error: mismatched input '<EOF>' expecting {'loop ', 'sla ', 'interacteer '}]");

    }

    @Test
    void loadAllConfigurationsTest() throws IOException {
        // Arrange
        Files.createFile(Paths.get(CONFIGURATIONS_PATH + "testConfigFile.txt"));
        File[] expectedFiles = new File[1];
        expectedFiles[0] = new File(CONFIGURATIONS_PATH + "testConfigFile.txt");

        // Act
        File[] files = agentService.loadAllConfigurations();

        // Assert
        assertArrayEquals(expectedFiles, files);
    }

    @Test
    void loadAllConfigurationsTest_NoFilesFound(){
        // Arrange
        String testPath = "src/test/java/resources/emptyFolder/";
        AgentService agentService = new AgentService(testPath);

        // Act
        File[] files = agentService.loadAllConfigurations();

        // Assert
        assertEquals(0, files.length);
    }

    @Test
    void deleteConfigurationTest() throws IOException {
        // Arrange
        String testFileName = "testConfigFile";
        String filePath = CONFIGURATIONS_PATH + testFileName;
        Path path = Paths.get(filePath);

        // Mock Files.deleteIfExists
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            when(Files.deleteIfExists(path)).thenReturn(true);

            // Act
            agentService.deleteConfiguration(testFileName);

            // Assert
            mockedFiles.verify(() -> Files.deleteIfExists(path));
        }
    }

    @Test
    void deleteConfigurationTest_FileNotFound() throws IOException {
        // Arrange
        String testFileName = "nonExistentFile";
        String filePath = CONFIGURATIONS_PATH + testFileName;
        Path path = Paths.get(filePath);

        // Mock Files.deleteIfExists to return false indicating file was not found
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            when(Files.deleteIfExists(path)).thenReturn(false);

            // Act
            agentService.deleteConfiguration(testFileName);

            // Assert
            mockedFiles.verify(() -> Files.deleteIfExists(path));
        }
    }

    @Test
    void deleteConfigurationTest_IOException() throws IOException {
        // Arrange
        String testFileName = "testConfigFile";
        String filePath = CONFIGURATIONS_PATH + testFileName;
        Path path = Paths.get(filePath);

        // Mock Files.deleteIfExists to throw an IOException
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            when(Files.deleteIfExists(path)).thenThrow(new IOException("File system error"));

            // Act
            agentService.deleteConfiguration(testFileName);

            // Assert
            mockedFiles.verify(() -> Files.deleteIfExists(path));
        }
    }
}