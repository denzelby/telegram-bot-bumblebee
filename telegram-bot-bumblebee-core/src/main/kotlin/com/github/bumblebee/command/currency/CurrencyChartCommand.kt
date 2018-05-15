package com.github.bumblebee.command.currency

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.currency.service.BYRExchangeRateChartService
import com.github.bumblebee.command.currency.service.BYRExchangeRateStoreService
import com.github.bumblebee.command.currency.service.ChartArgumentParser
import com.github.bumblebee.service.RandomPhraseService
import com.github.bumblebee.util.logger
import com.github.telegram.api.BotApi
import com.github.telegram.api.InputFile
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import org.xml.sax.SAXException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.time.LocalDate
import java.time.Period

@Component
class CurrencyChartCommand(protected val botApi: BotApi,
                           protected val randomPhrase: RandomPhraseService,
                           protected val exchangeRateStoreService: BYRExchangeRateStoreService,
                           protected val chartService: BYRExchangeRateChartService,
                           protected val argumentParser: ChartArgumentParser) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        val (from, to) = argumentParser.getRange(argument)
        validateRange(from, to)?.let { errorMessage ->
            botApi.sendMessage(chatId, errorMessage, update.message!!.messageId)
            return
        }
        val currencies = argumentParser.getCurrencies(argument)

        buildChart(update, from, to, currencies)
    }

    private fun validateRange(from: LocalDate, to: LocalDate): String? {
        val period = Period.between(from, to)
        return if (period.years > 3) {
            "Period too large"
        } else null
    }

    private fun buildChart(update: Update, from: LocalDate, to: LocalDate, currencies: List<String>) {
        val chatId = update.message!!.chat.id

        val period = Period.between(from, to)
        val approxDays = period.toTotalMonths() * 30 + period.days
        val detailed = approxDays < BAR_TO_LINE_CHART_THRESHOLD_DAYS
        val reduce = approxDays > REDUCE_THRESHOLD_DAYS

        try {
            var rates = exchangeRateStoreService.getRates(from, to, currencies)
            log.info("Fetched {} exchange rate records", rates.size)
            if (reduce) {
                rates = chartService.reduceToAverageByMonth(rates)
                log.info("{} records after reduction", rates.size)
            }

            val png = chartService.createChartImage(rates, detailed, from, to)
            botApi.sendPhoto(chatId, InputFile.photo(ByteArrayInputStream(png), "image"), null, null, null, null)
        } catch (e: IOException) {
            log.error("Chart creation failed", e)
            botApi.sendMessage(chatId, randomPhrase.no(), update.message!!.messageId)
        } catch (e: SAXException) {
            log.error("Chart creation failed", e)
            botApi.sendMessage(chatId, randomPhrase.no(), update.message!!.messageId)
        }
    }

    companion object {
        private val log = logger<CurrencyChartCommand>()

        private val BAR_TO_LINE_CHART_THRESHOLD_DAYS = 40
        private val REDUCE_THRESHOLD_DAYS = 5 * 30
    }

}
