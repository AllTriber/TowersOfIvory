package com.han.towersofivory.network.businesslayer.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SendFailedExceptionTest {

    @Test
    void testSendFailedExceptionWithMessage() {
        // Arrange
        String expectedMessage = "Failed to send data.";

        // Act
        SendFailedException exception = new SendFailedException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testSendFailedExceptionWithMessageAndCause() {
        // Arrange
        String expectedMessage = "Failed to send data.";
        Throwable expectedCause = new Throwable("Network connection lost");

        // Act
        SendFailedException exception = new SendFailedException(expectedMessage, expectedCause);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }
}
