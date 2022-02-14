package com.pjsun.MilCoevo.exception;

public class InactiveUserException extends RuntimeException {
    public InactiveUserException() {
        super();
    }

    public InactiveUserException(String message) {
        super(message);
    }

    public InactiveUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public InactiveUserException(Throwable cause) {
        super(cause);
    }

    protected InactiveUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
