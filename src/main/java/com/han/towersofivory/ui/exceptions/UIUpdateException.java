package com.han.towersofivory.ui.exceptions;

public class UIUpdateException extends RuntimeException {
    public UIUpdateException(String message) {
        super(message);
    }

    public UIUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
