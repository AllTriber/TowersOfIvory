package com.han.towersofivory.ui.exceptions;

public class UIServiceInterruptedException extends RuntimeException {
    public UIServiceInterruptedException(String message) {
        super(message);
    }

    public UIServiceInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}