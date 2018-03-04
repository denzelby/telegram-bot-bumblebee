package com.github.bumblebee.command.currency.service

import com.github.bumblebee.command.currency.config.CurrencyChartConfig
import com.github.bumblebee.command.currency.domain.SupportedCurrency
import com.google.common.collect.Lists
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@Service
class ChartArgumentParser(private val chartConfig: CurrencyChartConfig) {

    data class DateRange(val from: LocalDate, val to: LocalDate)

    fun getCurrencies(argument: String?): List<String> {
        if (argument.isNullOrEmpty()) {
            return chartConfig.defaultCurrencies
        }

        val tokenizer = StringTokenizer(argument, " ")
        val result = Lists.newArrayListWithExpectedSize<String>(tokenizer.countTokens())
        while (tokenizer.hasMoreTokens()) {
            val supportedCurrency = SupportedCurrency.parse(tokenizer.nextToken())
            if (supportedCurrency != null) {
                result.add(supportedCurrency.name)
            }
        }
        return if (result.isEmpty()) chartConfig.defaultCurrencies else result
    }

    fun getRange(argument: String?): DateRange {
        if (argument.isNullOrEmpty()) {
            return defaultRange()
        }

        val tokenizer = StringTokenizer(argument, " ")
        val dateFormatter = DateTimeFormatter.ofPattern(chartConfig.dateFormat)
        var from: LocalDate? = null
        var to: LocalDate? = null
        var yearFrom: Int? = null
        var yearTo: Int? = null

        val now = LocalDate.now()

        while (tokenizer.hasMoreTokens()) {
            val token = tokenizer.nextToken()
            try {
                val date = LocalDate.parse(token, dateFormatter)
                if (date.isAfter(now)) {
                    continue
                }
                if (from == null) {
                    from = date
                } else {
                    to = date
                    break
                }
            } catch (e: DateTimeParseException) {
                if (yearFrom == null) {
                    yearFrom = tryParseAsYear(token)
                } else if (yearTo == null) {
                    yearTo = tryParseAsYear(token)
                }
            }
        }

        // concrete date range
        if (from != null) {
            return rangeFrom(from, Optional.ofNullable(to).orElse(now))
        }

        // year range
        return if (yearFrom != null) {
            DateRange(
                    LocalDate.of(yearFrom, 1, 1),
                    LocalDate.of(Optional.ofNullable(yearTo).orElse(yearFrom), 12, 31))
        } else defaultRange()

        // no dates passed
    }

    private fun tryParseAsYear(token: String): Int? {
        return try {
            val number = Integer.parseUnsignedInt(token, 10)
            number.takeIf { number in MIN_SUPPORTED_YEAR..CURRENT_YEAR }
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun rangeFrom(date1: LocalDate, date2: LocalDate): DateRange {
        return if (date2.isBefore(date1)) {
            DateRange(date2, date1)
        } else DateRange(date1, date2)
    }

    private fun defaultRange(): DateRange {
        return DateRange(
                LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1),
                LocalDate.now()
        )
    }

    companion object {
        private val MIN_SUPPORTED_YEAR = 2000
        private val CURRENT_YEAR = LocalDate.now().year
    }
}
