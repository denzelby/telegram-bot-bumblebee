package com.github.bumblebee.command.currency

import com.github.bumblebee.command.currency.domain.SupportedCurrency
import com.github.bumblebee.command.currency.service.BYRExchangeRateRetrieveService
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class CurrencyUSDActualExchangeRateCommand(
        botApi: BotApi,
        randomPhrase: RandomPhraseService,
        exchangeRateRetrieveService: BYRExchangeRateRetrieveService)
    : CurrencyActualExchangeRateCommand(botApi, randomPhrase, exchangeRateRetrieveService) {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        super.handleCommand(update, chatId, SupportedCurrency.USD.name)
    }
}
