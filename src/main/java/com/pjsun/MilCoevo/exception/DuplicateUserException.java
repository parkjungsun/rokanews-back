package com.pjsun.MilCoevo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super();
    }
    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateUserException(String message) {
        super(message);
    }
    public DuplicateUserException(Throwable cause) {
        super(cause);
    }
    protected DuplicateUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
