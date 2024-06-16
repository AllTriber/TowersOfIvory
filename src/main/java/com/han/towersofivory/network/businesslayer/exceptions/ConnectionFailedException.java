package com.han.towersofivory.network.businesslayer.exceptions;

/**
 * ConnectionFailedException is a class that extends the Exception class.
 * It represents an exception that is thrown when a connection fails.
 */
public class ConnectionFailedException extends Exception {
    /**
     * Constructor for the ConnectionFailedException class.
     * Initializes the exception with a given message.
     *
     * @param message The message of the exception.
     */
    public ConnectionFailedException(String message) {
        super(message);
    }

    /**
     * Constructor for the ConnectionFailedException class.
     * Initializes the exception with a given message and cause.
     *
     * @param message The message of the exception.
     * @param cause   The cause of the exception.
     */
    public ConnectionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}