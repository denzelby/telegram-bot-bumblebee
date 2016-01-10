package com.github.bumblebee.command.currency;

import com.github.bumblebee.command.currency.domain.SupportedCurrency;
import com.github.bumblebee.command.currency.service.BYRExchangeRateRetrieveService;
import com.github.bumblebee.command.currency.service.CurrencyBidEvalService;
import com.github.bumblebee.service.RandomPhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

@Component
public class CurrencyUSDActualExchangeRateCommand extends CurrencyActualExchangeRateCommand {

    @Autowired
    public CurrencyUSDActualExchangeRateCommand(BotApi botApi, RandomPhraseService randomPhrase, CurrencyBidEvalService bidEvalService, BYRExchangeRateRetrieveService exchangeRateRetrieveService) {
        super(botApi, randomPhrase, bidEvalService, exchangeRateRetrieveService);
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {
        super.handleCommand(update, chatId, SupportedCurrency.USD.name());
    }
}
