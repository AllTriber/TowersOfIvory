package com.han.towersofivory.network.businesslayer.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionFailedExceptionTest {

    @Test
    void testConnectionFailedExceptionWithMessage() {
        // Arrange
        String expectedMessage = "Connection failed due to network issues.";

        // Act
        ConnectionFailedException exception = new ConnectionFailedException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testConnectionFailedExceptionWithMessageAndCause() {
        // Arrange
        String expectedMessage = "Connection failed due to network issues.";
        Throwable expectedCause = new Throwable("Network timeout");

        // Act
        ConnectionFailedException exception = new ConnectionFailedException(expectedMessage, expectedCause);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }
}
