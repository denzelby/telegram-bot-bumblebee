package com.github.bumblebee.command.brent

import com.github.bumblebee.bot.consumer.UpdateHandler
import com.github.bumblebee.command.brent.bitfinex.BitfinexProvider
import com.github.bumblebee.command.brent.bitfinex.BitfinexTicker
import com.github.bumblebee.command.brent.bitfinex.BitfinexTickerLabel
import com.github.bumblebee.command.brent.meduza.MeduzaStockProvider
import com.github.bumblebee.command.brent.meduza.MeduzaStockResponse
import com.github.bumblebee.util.loggerFor
import com.github.telegram.api.BotApi
import com.github.telegram.domain.ParseMode
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import java.text.DecimalFormat
import java.text.MessageFormat
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Component
class CurrenciesCommand(private val botApi: BotApi,
                        private val meduzaProvider: MeduzaStockProvider,
                        private val bitfinexProvider: BitfinexProvider) : UpdateHandler {

    private val valueFormatter = DecimalFormat("#,##0.00")
    private val deltaFormatter = DecimalFormat("+#,##0.00;-#,##0.00")

    override fun onUpdate(update: Update): Boolean {

        val meduzaFuture = CompletableFuture.supplyAsync { meduzaProvider.getCurrentStocks() }
                .thenApply { buildResponse(it) }

        val bitfinexFuture = CompletableFuture.supplyAsync { bitfinexProvider.loadTickers(tickersToLoad) }
                .thenApply { buildResponse(it) }

        val message = bitfinexFuture.getOrElse("Bitfinex seems to be broken...", 2) + "\n\n" +
                meduzaFuture.getOrElse("Meduza.io seems to be broken...", 2)

        botApi.sendMessage(update.message!!.chat.id, message, ParseMode.MARKDOWN)
        return true
    }

    private fun <T> CompletableFuture<T>.getOrElse(recover: T, timeout: Long, unit: TimeUnit = TimeUnit.SECONDS): T {
        return try {
            exceptionally { recover }.get(timeout, unit)
        } catch (e: RuntimeException) {
            log.error("Future failed", e)
            recover
        }
    }

    private fun buildResponse(tickers: List<BitfinexTicker>): String {
        return tickersToLoad.zip(tickers).joinToString("\n") { (label, ticker) ->
            "*${label.title}*: ${ticker.lastPrice} (${deltaFormatter.format(ticker.dailyChange)})"
        }
    }

    private fun buildResponse(stocks: MeduzaStockResponse): String {

        val brentDelta = stocks.brent.current - stocks.brent.prev
        val usdDelta = stocks.usd.current - stocks.usd.prev
        val eurDelta = stocks.eur.current - stocks.eur.prev

        return line("Brent", stocks.brent.current, brentDelta, DOLLAR_SYMBOL) +
                line("Usd", stocks.usd.current, usdDelta, RUB_SYMBOL) +
                line("Eur", stocks.eur.current, eurDelta, RUB_SYMBOL)
    }

    private fun line(name: String, value: Float, delta: Float, symbol: Char): String {

        var result = MessageFormat.format("*{0}*: {1} {2}", name, valueFormatter.format(value.toDouble()), symbol)

        if (delta != 0f && delta != value) {
            result += " (" + deltaFormatter.format(delta.toDouble()) + ")"
        }
        return result + "\n"
    }

    companion object {
        private val log = loggerFor<CurrenciesCommand>()

        private val tickersToLoad = BitfinexTickerLabel.values().toList()

        private val DOLLAR_SYMBOL = '$'
        private val RUB_SYMBOL = '\u20BD'
    }
}
