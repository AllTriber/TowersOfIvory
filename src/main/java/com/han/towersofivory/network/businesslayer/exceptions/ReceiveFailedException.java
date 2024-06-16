package com.han.towersofivory.network.businesslayer.exceptions;

/**
 * ReceiveFailedException is a class that extends the Exception class.
 * It represents an exception that is thrown when a receive operation fails.
 */
public class ReceiveFailedException extends Exception {
    /**
     * Constructor for the ReceiveFailedException class.
     * Initializes the exception with a given message.
     *
     * @param message The message of the exception.
     */
    public ReceiveFailedException(String message) {
        super(message);
    }

    /**
     * Constructor for the ReceiveFailedException class.
     * Initializes the exception with a given message and cause.
     *
     * @param message The message of the exception.
     * @param cause   The cause of the exception.
     */
    public ReceiveFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}