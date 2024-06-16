package com.han.towersofivory.network.businesslayer.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatchPacketExceptionTest {

    @Test
    void testDispatchPacketExceptionWithMessage() {
        // Arrange
        String expectedMessage = "Packet dispatch failed due to invalid packet format.";

        // Act
        DispatchPacketException exception = new DispatchPacketException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testDispatchPacketExceptionWithMessageAndCause() {
        // Arrange
        String expectedMessage = "Packet dispatch failed due to invalid packet format.";
        Throwable expectedCause = new Throwable("Invalid format");

        // Act
        DispatchPacketException exception = new DispatchPacketException(expectedMessage, expectedCause);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }
}
