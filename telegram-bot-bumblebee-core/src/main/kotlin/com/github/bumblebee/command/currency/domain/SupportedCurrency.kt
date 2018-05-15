package com.github.bumblebee.command.currency.domain

enum class SupportedCurrency {
    USD,
    EUR,
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

    companion object {
        fun parse(currency: String?): SupportedCurrency? {
            return currency?.let {
                SupportedCurrency.values().firstOrNull { it.name.equals(currency, ignoreCase = true) }
            }
        }
    }
}
