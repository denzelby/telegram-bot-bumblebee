package com.github.bumblebee.command.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.bumblebee.command.currency.domain.SupportedCurrency;
import com.github.bumblebee.command.currency.service.BYRExchangeRateRetrieveService;
import com.github.bumblebee.command.currency.service.CurrencyBidEvalService;
import com.github.bumblebee.service.RandomPhraseService;

import com.github.telegram.api.BotApi;
import com.github.telegram.domain.Update;

@Component
public class CurrencyUSDActualExchangeRateCommand extends CurrencyActualExchangeRateCommand {

    @Autowired
    public CurrencyUSDActualExchangeRateCommand(BotApi botApi, RandomPhraseService randomPhrase, CurrencyBidEvalService bidEvalService, BYRExchangeRateRetrieveService exchangeRateRetrieveService) {
        super(botApi, randomPhrase, bidEvalService, exchangeRateRetrieveService);
    }

    @Override
    public void handleCommand(Update update, long chatId, String argument) {
        super.handleCommand(update, chatId, SupportedCurrency.USD.name());
    }
}
