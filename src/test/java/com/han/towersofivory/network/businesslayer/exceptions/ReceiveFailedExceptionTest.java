package com.han.towersofivory.network.businesslayer.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiveFailedExceptionTest {

    @Test
    void testReceiveFailedExceptionWithMessage() {
        // Arrange
        String expectedMessage = "Receive operation failed due to network issues.";

        // Act
        ReceiveFailedException exception = new ReceiveFailedException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testReceiveFailedExceptionWithMessageAndCause() {
        // Arrange
        String expectedMessage = "Receive operation failed due to network issues.";
        Throwable expectedCause = new Throwable("Network timeout");

        // Act
        ReceiveFailedException exception = new ReceiveFailedException(expectedMessage, expectedCause);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }
}
