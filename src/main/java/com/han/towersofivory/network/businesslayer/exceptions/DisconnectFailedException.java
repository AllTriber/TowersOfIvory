package com.han.towersofivory.network.businesslayer.exceptions;

/**
 * DisconnectFailedException is a class that extends the Exception class.
 * It represents an exception that is thrown when a disconnection fails.
 */
public class DisconnectFailedException extends Exception {
    /**
     * Constructor for the DisconnectFailedException class.
     * Initializes the exception with a given message.
     *
     * @param message The message of the exception.
     */
    public DisconnectFailedException(String message) {
        super(message);
    }

    /**
     * Constructor for the DisconnectFailedException class.
     * Initializes the exception with a given message and cause.
     *
     * @param message The message of the exception.
     * @param cause   The cause of the exception.
     */
    public DisconnectFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}