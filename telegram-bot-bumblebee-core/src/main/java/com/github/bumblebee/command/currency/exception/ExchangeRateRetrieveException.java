package com.github.bumblebee.command.currency.exception;

public class ExchangeRateRetrieveException extends RuntimeException {
    public ExchangeRateRetrieveException() {
    }

    public ExchangeRateRetrieveException(String message) {
        super(message);
    }

    public ExchangeRateRetrieveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeRateRetrieveException(Throwable cause) {
        super(cause);
    }

    public ExchangeRateRetrieveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
