package com.github.bumblebee.command.currency.domain;

public enum SupportedCurrency {
    USD, EUR,

    AUD,
    BGN,
    UAH,
    DKK,
    PLN,
    IRR,
    ISK,
    JPY,
    CAD,
    CNY,
    KWD,
    MDL,
    NZD,
    NOK,
    RUB,
    XDR,
    SGD,
    KGS,
    KZT,
    TRY,
    GBP,
    CZK,
    SEK,
    CHF;

    public static SupportedCurrency parse(String currency) {
        if (currency == null) {
            return null;
        }

        for (SupportedCurrency supportedCurrency : SupportedCurrency.values()) {
            if (supportedCurrency.name().equalsIgnoreCase(currency)) {
                return supportedCurrency;
            }
        }
        return null;
    }
}
