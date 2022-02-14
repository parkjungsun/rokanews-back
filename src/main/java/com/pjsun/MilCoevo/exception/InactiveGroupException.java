package com.pjsun.MilCoevo.exception;

public class InactiveGroupException extends RuntimeException{
    public InactiveGroupException() {
        super();
    }
    public InactiveGroupException(String message, Throwable cause) {
        super(message, cause);
    }
    public InactiveGroupException(String message) {
        super(message);
    }
    public InactiveGroupException(Throwable cause) {
        super(cause);
    }
    protected InactiveGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
