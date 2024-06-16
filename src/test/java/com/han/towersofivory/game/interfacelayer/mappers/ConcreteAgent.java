package com.han.towersofivory.game.interfacelayer.mappers;

import com.han.towersofivory.agent.businesslayer.services.AgentService;
import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.agent.interfacelayer.mappers.ConcreteAgent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class ConcreteAgentTest {

    String testFileName = "testFile";

    @Mock
    AgentService agentService;

    @InjectMocks
    ConcreteAgent concreteAgent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        Files.write(Paths.get("src/main/resources/agentConfigurations/testFile.txt"), "test".getBytes());
    }

    @AfterAll
    static void afterAll() throws IOException {
        Files.deleteIfExists(Paths.get("src/main/resources/agentConfigurations/testFile.txt"));
    }


    @Test
    void saveConfigurationTest() {
        // Arrange
        Path path = Paths.get("src/main/resources/agentConfigurations/" + testFileName + ".txt");

        // Mock Files.write
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            byte[] expectedContent = "configuration".getBytes();
            mockedFiles.when(() -> Files.write(path, expectedContent)).thenReturn(path);

            // Act
            concreteAgent.saveConfiguration(new AgentConfigurationText(testFileName, "configuration"));

            // Assert
            mockedFiles.verify(() -> Files.write(path, expectedContent));
        }
    }

    @Test
    void loadAllConfigurations() {
        // Arrange
        when(agentService.loadAllConfigurations()).thenReturn(new File[0]);

        // Act
        File[] files = concreteAgent.loadAllConfigurations();

        // Assert
        assertNotEquals(0, files.length);
    }

    @Test
    void deleteConfigurationTest() throws IOException {
        // Arrange
        Path path = Paths.get("src/main/resources/agentConfigurations/" + testFileName);

        // Mock Files.deleteIfExists
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            when(Files.deleteIfExists(path)).thenReturn(true);

            // Act
            concreteAgent.deleteConfiguration(testFileName);

            // Assert
            mockedFiles.verify(() -> Files.deleteIfExists(path));
        }
    }
}