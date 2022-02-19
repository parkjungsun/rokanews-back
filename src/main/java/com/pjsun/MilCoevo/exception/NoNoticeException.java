package com.pjsun.MilCoevo.exception;

public class NoNoticeException extends RuntimeException {
    public NoNoticeException() {
        super();
    }

    public NoNoticeException(String message) {
        super(message);
    }

    public NoNoticeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoNoticeException(Throwable cause) {
        super(cause);
    }

    protected NoNoticeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
