package com.github.bumblebee.command.currency.service

import com.github.bumblebee.command.currency.dataprovider.nbrb.NBRBExRatesParser
import com.github.bumblebee.command.currency.domain.SupportedCurrency
import com.github.bumblebee.command.currency.exception.ExchangeRateRetrieveException
import org.springframework.stereotype.Service
import org.xml.sax.SAXException
import java.io.IOException
import java.time.LocalDate

@Service
class BYRExchangeRateRetrieveService(private val parser: NBRBExRatesParser) {

    fun getCurrentExchangeRate(currencyName: String): Double? {
        return getExchangeRate(LocalDate.now(), currencyName)
    }

    fun getExchangeRate(date: LocalDate, currency: SupportedCurrency): Double? {
        return getExchangeRate(date, currency.name)
    }

    fun getExchangeRate(date: LocalDate, currency: String): Double? {
        try {
            return parser.getDailyRates(date)
                    .firstOrNull { currency.toUpperCase() == it.shortName }?.rate
        } catch (e: IOException) {
            throw ExchangeRateRetrieveException(e)
        } catch (e: SAXException) {
            throw ExchangeRateRetrieveException(e)
        }
    }

}
