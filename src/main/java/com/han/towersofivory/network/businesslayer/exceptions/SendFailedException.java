package com.han.towersofivory.network.businesslayer.exceptions;

/**
 * SendFailedException is a class that extends the Exception class.
 * It represents an exception that is thrown when a send operation fails.
 */
public class SendFailedException extends Exception {
    /**
     * Constructor for the SendFailedException class.
     * Initializes the exception with a given message.
     *
     * @param message The message of the exception.
     */
    public SendFailedException(String message) {
        super(message);
    }

    /**
     * Constructor for the SendFailedException class.
     * Initializes the exception with a given message and cause.
     *
     * @param message The message of the exception.
     * @param cause   The cause of the exception.
     */
    public SendFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}