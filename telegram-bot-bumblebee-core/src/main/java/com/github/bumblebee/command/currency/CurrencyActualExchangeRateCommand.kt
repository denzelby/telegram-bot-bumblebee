package com.github.bumblebee.command.currency

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.currency.domain.SupportedCurrency
import com.github.bumblebee.command.currency.service.BYRExchangeRateRetrieveService
import com.github.bumblebee.service.RandomPhraseService
import com.github.bumblebee.util.logger
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import java.text.MessageFormat

@Component
class CurrencyActualExchangeRateCommand(
        private val botApi: BotApi,
        private val randomPhrase: RandomPhraseService,
        private val exchangeRateRetrieveService: BYRExchangeRateRetrieveService) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        val currencyName = getCurrencyNameOrDefault(argument, SupportedCurrency.USD)
        try {
            val rate = exchangeRateRetrieveService.getCurrentExchangeRate(currencyName)
            if (rate != null) {
                val message = MessageFormat.format("BYR/{0}: {1}", currencyName, rate)
                botApi.sendMessage(chatId, message)
            } else {
                botApi.sendMessage(chatId, "NBRB doesn't know.", update.message!!.messageId)
            }
        } catch (e: Exception) {
            log.error("Currency retrieve failed for " + currencyName, e)
            botApi.sendMessage(chatId, randomPhrase.no(), update.message!!.messageId)
        }
    }

    private fun getCurrencyNameOrDefault(argument: String?, defaultCurrency: SupportedCurrency): String {
        return if (!argument.isNullOrEmpty()) argument!!.trim { it <= ' ' }.toUpperCase()
        else defaultCurrency.name
    }

    companion object {
        private val log = logger<CurrencyActualExchangeRateCommand>()
    }
}
