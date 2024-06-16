package com.han.towersofivory.game.businesslayer.worldgeneration;

import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.Gamemode;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.ui.interfacelayer.mappers.UIMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class GameWorldServiceTest {

    @Mock
    private UIMapper uiMapper;
    private GameMapper gameMapper;
    private GameWorldService gameWorldService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        gameWorldService = new GameWorldService(uiMapper, gameMapper);
    }

    @Test
    void seedIsEmptyUsesCurrentTimeMillis() {
        // Arrange

        String seed = "";

        // Act
        long actual = gameWorldService.getSeed(seed);

        // Assert
        assertNotEquals(0, actual);
    }


    @ParameterizedTest
    @CsvSource({
            "0",
            "1",
            "1234567890",
            "1234567890123456789",
            "-2"
    })
    void seedIsNotEmptyParsedAsLong(String expectedSeed) {
        // Arrange

        // Act
        long actual = gameWorldService.getSeed(expectedSeed);

        // Assert
        assertEquals(actual, Long.parseLong(expectedSeed));
    }

    @Test
    void seedIsNotANumberThrowsNumberFormatException() {
        // Arrange
        String seed = "notANumber";

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> gameWorldService.getSeed(seed));
    }

    @Test
    void gameStateIsSet() {
        //Arrange
        GameConfiguration gameConfiguration = new GameConfiguration("test", Gamemode.LMS, 123);
        GameInfo gameInfo = new GameInfo(gameConfiguration, 1);
        GameState gameState = new GameState(gameInfo);

        //Act
        gameWorldService.setGameState(gameState);

        //Assert
        assertNotNull(gameWorldService.getGameState());
    }
}
