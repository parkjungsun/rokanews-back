package com.pjsun.MilCoevo.exception;

public class NoCommentException extends RuntimeException{
    public NoCommentException() {
        super();
    }

    public NoCommentException(String message) {
        super(message);
    }

    public NoCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCommentException(Throwable cause) {
        super(cause);
    }

    protected NoCommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
