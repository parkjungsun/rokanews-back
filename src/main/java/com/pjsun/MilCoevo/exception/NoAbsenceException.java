package com.pjsun.MilCoevo.exception;

public class NoAbsenceException extends RuntimeException {
    public NoAbsenceException() {
        super();
    }

    public NoAbsenceException(String message) {
        super(message);
    }

    public NoAbsenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAbsenceException(Throwable cause) {
        super(cause);
    }

    protected NoAbsenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
