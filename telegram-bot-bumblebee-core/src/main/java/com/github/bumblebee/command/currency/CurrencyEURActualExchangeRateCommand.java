package com.github.bumblebee.command.currency;

import com.github.bumblebee.command.currency.domain.SupportedCurrency;
import com.github.bumblebee.command.currency.service.BYRExchangeRateRetrieveService;
import com.github.bumblebee.service.RandomPhraseService;
import com.github.telegram.api.BotApi;
import com.github.telegram.domain.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyEURActualExchangeRateCommand extends CurrencyActualExchangeRateCommand {

    @Autowired
    public CurrencyEURActualExchangeRateCommand(BotApi botApi,
                                                RandomPhraseService randomPhrase,
                                                BYRExchangeRateRetrieveService exchangeRateRetrieveService) {
        super(botApi, randomPhrase, exchangeRateRetrieveService);
    }

    @Override
    public void handleCommand(Update update, long chatId, String argument) {
        super.handleCommand(update, chatId, SupportedCurrency.EUR.name());
    }
}
