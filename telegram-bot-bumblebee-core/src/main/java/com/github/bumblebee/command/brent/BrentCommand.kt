package com.github.bumblebee.command.brent

import com.github.bumblebee.command.brent.meduza.MeduzaStockProvider
import com.github.bumblebee.command.brent.meduza.MeduzaStockResponse
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import telegram.polling.UpdateHandler
import java.text.DecimalFormat
import java.text.MessageFormat

@Component
class BrentCommand(private val botApi: BotApi,
                   private val stockProvider: MeduzaStockProvider) : UpdateHandler {

    private val valueFormatter = DecimalFormat("#,##0.00")
    private val deltaFormatter = DecimalFormat("+#,##0.00;-#,##0.00")

    override fun onUpdate(update: Update): Boolean {
        val chatId = update.message!!.chat.id

        val stocks = try {
            this.stockProvider.currentStocks
        } catch (e: Exception) {
            log.error("Failed to retrieve stocks", e)
            botApi.sendMessage(chatId, "Meduza.io seems to be broken...").execute()
            return true
        }

        botApi.sendMessage(chatId, buildResponse(stocks), "markdown").execute()

        return true
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

        if (delta != 0f) {
            result += " (" + deltaFormatter.format(delta.toDouble()) + ")"
        }
        return result + "\n"
    }

    companion object {
        private val log = LoggerFactory.getLogger(BrentCommand::class.java)

        private val DOLLAR_SYMBOL = '$'
        private val RUB_SYMBOL = '\u20BD'
    }
}
