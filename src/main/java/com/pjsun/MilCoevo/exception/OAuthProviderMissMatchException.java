package com.pjsun.MilCoevo.exception;

public class OAuthProviderMissMatchException extends RuntimeException{
    public OAuthProviderMissMatchException() {
        super();
    }

    public OAuthProviderMissMatchException(String message) {
        super(message);
    }

    public OAuthProviderMissMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthProviderMissMatchException(Throwable cause) {
        super(cause);
    }

    protected OAuthProviderMissMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
