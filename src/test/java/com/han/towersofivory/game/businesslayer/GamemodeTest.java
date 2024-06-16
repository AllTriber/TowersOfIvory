package com.han.towersofivory.game.businesslayer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamemodeTest {

    @Test
    void testGetFullName() {
        assertEquals("Last Man Standing", Gamemode.LMS.getFullName());
        assertEquals("Capture the Flag", Gamemode.CTF.getFullName());
    }

    @Test
    void testFromFullName() {
        assertEquals(Gamemode.LMS, Gamemode.fromFullName("Last Man Standing"));
        assertEquals(Gamemode.CTF, Gamemode.fromFullName("Capture the Flag"));
    }

    @Test
    void testFromFullNameWithInvalidName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Gamemode.fromFullName("Invalid Name");
        });

        String expectedMessage = "No enum constant com.han.towersofivory.game.businesslayer.Gamemode with full name Invalid Name";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
