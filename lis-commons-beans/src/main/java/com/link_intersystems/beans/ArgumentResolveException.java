package com.link_intersystems.beans;

public class ArgumentResolveException extends Exception {

    public ArgumentResolveException() {
    }

    public ArgumentResolveException(String message) {
        super(message);
    }

    public ArgumentResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentResolveException(Throwable cause) {
        super(cause);
    }

    public ArgumentResolveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
