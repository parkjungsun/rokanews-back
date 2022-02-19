package com.pjsun.MilCoevo.exception;

public class NoScheduleException extends RuntimeException {
    public NoScheduleException() {
        super();
    }

    public NoScheduleException(String message) {
        super(message);
    }

    public NoScheduleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoScheduleException(Throwable cause) {
        super(cause);
    }

    protected NoScheduleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
