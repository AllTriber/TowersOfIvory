package com.han.towersofivory.network.businesslayer.exceptions;

/**
 * DispatchPacketException is a class that extends the Exception class.
 * It represents an exception that is thrown when a packet dispatch fails.
 */
public class DispatchPacketException extends Exception {
    /**
     * Constructor for the DispatchPacketException class.
     * Initializes the exception with a given message.
     *
     * @param message The message of the exception.
     */
    public DispatchPacketException(String message) {
        super(message);
    }

    /**
     * Constructor for the DispatchPacketException class.
     * Initializes the exception with a given message and cause.
     *
     * @param message The message of the exception.
     * @param cause   The cause of the exception.
     */
    public DispatchPacketException(String message, Throwable cause) {
        super(message, cause);
    }
}