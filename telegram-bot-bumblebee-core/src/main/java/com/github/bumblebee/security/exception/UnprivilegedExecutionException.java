package com.github.bumblebee.security.exception;

public class UnprivilegedExecutionException extends RuntimeException {

    public UnprivilegedExecutionException() {
    }

    public UnprivilegedExecutionException(String message) {
        super(message);
    }

    public UnprivilegedExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprivilegedExecutionException(Throwable cause) {
        super(cause);
    }

    public UnprivilegedExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
