package com.github.bumblebee.command.currency;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.currency.domain.SupportedCurrency;
import com.github.bumblebee.command.currency.service.BYRExchangeRateRetrieveService;
import com.github.bumblebee.service.RandomPhraseService;
import com.github.telegram.api.BotApi;
import com.github.telegram.domain.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

@Component
public class CurrencyActualExchangeRateCommand extends SingleArgumentCommand {

    private static final Logger log = LoggerFactory.getLogger(CurrencyActualExchangeRateCommand.class);

    private final BotApi botApi;
    private final RandomPhraseService randomPhrase;
    private final BYRExchangeRateRetrieveService exchangeRateRetrieveService;

    @Autowired
    public CurrencyActualExchangeRateCommand(BotApi botApi,
                                             RandomPhraseService randomPhrase,
                                             BYRExchangeRateRetrieveService exchangeRateRetrieveService) {
        this.botApi = botApi;
        this.randomPhrase = randomPhrase;
        this.exchangeRateRetrieveService = exchangeRateRetrieveService;
    }

    @Override
    public void handleCommand(Update update, long chatId, String argument) {

        String currencyName = getCurrencyNameOrDefault(argument, SupportedCurrency.USD);
        try {
            Double rate = exchangeRateRetrieveService.getCurrentExchangeRate(currencyName);
            if (rate != null) {
                String message = MessageFormat.format("BYR/{0}: {1}", currencyName, rate);
                botApi.sendMessage(chatId, message);
            } else {
                botApi.sendMessage(chatId, "NBRB doesn't know.", update.getMessage().getMessageId());
            }
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
