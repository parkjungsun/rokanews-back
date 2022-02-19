package com.pjsun.MilCoevo.exception;

public class MaxMemberException extends RuntimeException {
    public MaxMemberException() {
        super();
    }

    public MaxMemberException(String message) {
        super(message);
    }

    public MaxMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxMemberException(Throwable cause) {
        super(cause);
    }

    protected MaxMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
