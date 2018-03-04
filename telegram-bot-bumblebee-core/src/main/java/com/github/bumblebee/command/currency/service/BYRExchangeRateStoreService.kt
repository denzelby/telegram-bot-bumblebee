package com.github.bumblebee.command.currency.service

import com.github.bumblebee.command.currency.dao.DailyRateRepository
import com.github.bumblebee.command.currency.dataprovider.nbrb.Currency
import com.github.bumblebee.command.currency.dataprovider.nbrb.NBRBExRatesParser
import com.github.bumblebee.command.currency.domain.DailyExchangeRate
import com.github.bumblebee.util.logger
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import org.xml.sax.SAXException
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Service
class BYRExchangeRateStoreService(
        private val parser: NBRBExRatesParser,
        private val repository: DailyRateRepository) {

    @Throws(IOException::class, SAXException::class)
    fun getRates(from: LocalDate, to: LocalDate, currencies: List<String>): List<DailyExchangeRate> {

        Assert.isTrue(from.isBefore(to) || from.isEqual(to), "'to' cannot be after 'from' date")

        val dbRates = repository.findByDateBetweenAndCurrencyIn(toDate(from), toDate(to), currencies)
        val dateRange = createDateRange(from, to)
        val dbRatesDates = dbRates.map { rate -> toLocalDate(rate.date) }.toSet()
        val ratesToFetch = Sets.difference(dateRange, dbRatesDates)

        if (ratesToFetch.size > 0) {
            log.info("Rates need to be fetched", ratesToFetch.size)
            if (log.isTraceEnabled) {
                ratesToFetch.forEach { rate -> log.trace(rate.toString()) }
            }

            val fetchedRates = fetchRates(ratesToFetch)
            return mergeRates(dbRates, fetchedRates, currencies)
        }
        return dbRates
    }

    private fun mergeRates(dbRates: List<DailyExchangeRate>, fetchedRates: List<DailyExchangeRate>,
                           currencies: List<String>): List<DailyExchangeRate> {
        val currencySet = HashSet(currencies)
        val filteredRates = fetchedRates.filter { rate -> currencySet.contains(rate.currency) }

        val result = Lists.newArrayListWithExpectedSize<DailyExchangeRate>(dbRates.size + filteredRates.size)
        result.addAll(dbRates)
        result.addAll(filteredRates)
        return result
    }

    @Throws(IOException::class, SAXException::class)
    private fun fetchRates(ratesToFetch: Set<LocalDate>): List<DailyExchangeRate> {

        val result = ArrayList<DailyExchangeRate>()

        for (dateToFetch in ratesToFetch) {
            val fetchedRates = parser.getDailyRates(dateToFetch)
            val storedRates = storeRates(fetchedRates, dateToFetch)

            result.addAll(storedRates)
        }
        return result
    }

    private fun storeRates(rates: List<Currency>, localDate: LocalDate): List<DailyExchangeRate> {
        val date = toDate(localDate)
        val ratesToSave = rates.map { currency -> DailyExchangeRate(date, currency.shortName, currency.rate) }

        repository.saveAll(ratesToSave)
        return ratesToSave
    }

    private fun createDateRange(from: LocalDate, to: LocalDate): Set<LocalDate> {
        val range = HashSet<LocalDate>()
        var cursor = from

        while (cursor.isBefore(to) || cursor.isEqual(to)) {
            range.add(cursor)
            cursor = cursor.plusDays(1)
        }
        return range
    }

    private fun toDate(localDate: LocalDate): Date {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    private fun toLocalDate(date: Date): LocalDate {
        return Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    companion object {
        private val log = logger<BYRExchangeRateStoreService>()
    }
}
