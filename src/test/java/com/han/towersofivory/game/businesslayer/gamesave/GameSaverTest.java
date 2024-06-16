package com.han.towersofivory.game.businesslayer.gamesave;

import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.Gamemode;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameSaverTest {

    private static final String V1_FILE_NAME = "gameStateV1";

    @BeforeAll
    static void setup() {
        // Create the directory if it doesn't exist
        new File("src/main/resources/saveGames").mkdirs();
    }

    @AfterEach
    void tearDown() {
        // Cleanup saved files after each test
        new File("src/main/resources/saveGames/" + V1_FILE_NAME + ".ser").delete();
    }

    @Test
    void testSaveAndLoadGameState() throws IOException, ClassNotFoundException {
        //Arrange
        GameConfiguration gameConfiguration = new GameConfiguration(V1_FILE_NAME, Gamemode.LMS, 123456);
        GameInfo gameInfo = new GameInfo(gameConfiguration, 1);
        GameState gameState = new GameState(gameInfo);

        GameSaver.saveGameState(gameState);
        GameState loadedGameStateV1 = GameSaver.loadGameState(V1_FILE_NAME);

        assertNotNull(loadedGameStateV1);
        assertEquals(gameState.getSerialVersionUID(), loadedGameStateV1.getSerialVersionUID());
    }

}
