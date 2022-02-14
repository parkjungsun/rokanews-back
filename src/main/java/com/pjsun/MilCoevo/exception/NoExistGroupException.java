package com.pjsun.MilCoevo.exception;

public class NoExistGroupException extends RuntimeException{
    public NoExistGroupException() {
        super();
    }

    public NoExistGroupException(String message) {
        super(message);
    }

    public NoExistGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoExistGroupException(Throwable cause) {
        super(cause);
    }

    protected NoExistGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
