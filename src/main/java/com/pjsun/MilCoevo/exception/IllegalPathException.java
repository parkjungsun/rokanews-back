package com.pjsun.MilCoevo.exception;

public class IllegalPathException extends RuntimeException {
    public IllegalPathException() {
        super();
    }

    public IllegalPathException(String message) {
        super(message);
    }

    public IllegalPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPathException(Throwable cause) {
        super(cause);
    }

    protected IllegalPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
