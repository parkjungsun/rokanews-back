package com.pjsun.MilCoevo.exception;

public class NoRefreshTokenException extends RuntimeException {
    public NoRefreshTokenException() {
        super();
    }

    public NoRefreshTokenException(String message) {
        super(message);
    }

    public NoRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRefreshTokenException(Throwable cause) {
        super(cause);
    }

    protected NoRefreshTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
