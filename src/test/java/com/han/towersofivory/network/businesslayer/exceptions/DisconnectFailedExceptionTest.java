package com.han.towersofivory.network.businesslayer.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisconnectFailedExceptionTest {

    @Test
    void testDisconnectFailedExceptionWithMessage() {
        // Arrange
        String expectedMessage = "Disconnection failed due to network issues.";

        // Act
        DisconnectFailedException exception = new DisconnectFailedException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testDisconnectFailedExceptionWithMessageAndCause() {
        // Arrange
        String expectedMessage = "Disconnection failed due to network issues.";
        Throwable expectedCause = new Throwable("Network timeout");

        // Act
        DisconnectFailedException exception = new DisconnectFailedException(expectedMessage, expectedCause);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }
}
