package com.han.towersofivory.game.businesslayer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameInfoTest {

    @Test
    void testGameInfo() {
        //Arrange/Act
        String gameName = "Spel";
        Gamemode gamemode = Gamemode.LMS;
        long seed = 1;
        int playerCount = 2;
        GameConfiguration gameConfiguration = new GameConfiguration(gameName, gamemode, seed);
        var sut = new GameInfo(gameConfiguration, playerCount);

        //Assert
        assertEquals("Spel", sut.getGameConfiguration().getName());
        assertEquals("LMS", sut.getGameConfiguration().getGamemode().toString());
        assertEquals(1, sut.getGameConfiguration().getSeed());
        assertEquals(2, sut.getNumberOfPlayers());
    }
}
