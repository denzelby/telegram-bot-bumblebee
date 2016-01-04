package com.github.bumblebee.command.currency;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.currency.domain.SupportedCurrency;
import com.github.bumblebee.command.currency.service.ByrExchangeRateRetrieveService;
import com.github.bumblebee.command.currency.service.CurrencyBidEvalService;
import com.github.bumblebee.service.RandomPhraseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.text.MessageFormat;

@Component
public class CurrencyActualExchangeRateCommand extends SingleArgumentCommand {

    private static final Logger log = LoggerFactory.getLogger(CurrencyActualExchangeRateCommand.class);

    private final BotApi botApi;
    private final RandomPhraseService randomPhrase;
    private final ByrExchangeRateRetrieveService exchangeRateRetrieveService;

    @Autowired
    public CurrencyActualExchangeRateCommand(BotApi botApi, RandomPhraseService randomPhrase,
                                             CurrencyBidEvalService bidEvalService,
                                             ByrExchangeRateRetrieveService exchangeRateRetrieveService) {
        this.botApi = botApi;
        this.randomPhrase = randomPhrase;
        this.exchangeRateRetrieveService = exchangeRateRetrieveService;
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {

        String currencyName = getCurrencyNameOrDefault(argument, SupportedCurrency.USD);
        try {
            int rate = (int) exchangeRateRetrieveService.getCurrentExchangeRate(currencyName);
            String message = MessageFormat.format("BYR/{0}: {1}", currencyName, rate);
            botApi.sendMessage(chatId, message);
        } catch (Exception e) {
            log.error("Currency retrieve failed for " + currencyName, e);
            botApi.sendMessage(chatId, randomPhrase.no(), update.getMessage().getMessageId());
        }
    }

    private String getCurrencyNameOrDefault(String argument, SupportedCurrency defaultCurrency) {
        if (StringUtils.isEmpty(argument)) {
            return defaultCurrency.name();
        }
        return argument.trim().toUpperCase();
    }

}
