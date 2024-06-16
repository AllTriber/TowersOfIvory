package com.han.towersofivory.game.businesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameConfigurationTest {

    private static final String TEST_NAME = "Test Game";
    private Gamemode testGamemode;
    private long seed;

    @BeforeEach
    void setUp() {
        testGamemode = Gamemode.LMS;  // Use an actual enum value
        seed = 1;  // Use an actual enum value
    }

    @Test
    void testConstructorAndGetters() {
        GameConfiguration gameConfiguration = new GameConfiguration(TEST_NAME, testGamemode, 1);

        assertEquals(TEST_NAME, gameConfiguration.getName());
        assertEquals(testGamemode, gameConfiguration.getGamemode());
        assertEquals(seed, gameConfiguration.getSeed());
    }
}
