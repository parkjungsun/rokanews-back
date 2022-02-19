package com.pjsun.MilCoevo.exception;

public class NoPurchaseException extends RuntimeException {
    public NoPurchaseException() {
        super();
    }

    public NoPurchaseException(String message) {
        super(message);
    }

    public NoPurchaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPurchaseException(Throwable cause) {
        super(cause);
    }

    protected NoPurchaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
