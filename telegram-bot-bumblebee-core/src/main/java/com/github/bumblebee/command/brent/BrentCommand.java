package com.github.bumblebee.command.brent;

import com.github.bumblebee.command.brent.meduza.MeduzaStockProvider;
import com.github.bumblebee.command.brent.meduza.response.MeduzaStockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;
import telegram.domain.request.ParseMode;
import telegram.polling.UpdateHandler;

import java.text.DecimalFormat;
import java.text.MessageFormat;

@Component
public class BrentCommand implements UpdateHandler {

    private static final Logger log = LoggerFactory.getLogger(BrentCommand.class);

    private static final char DOLLAR_SYMBOL = '$';
    private static final char RUB_SYMBOL = '\u20BD';

    private final DecimalFormat valueFormatter = new DecimalFormat("#,##0.00");
    private final DecimalFormat deltaFormatter = new DecimalFormat("+#,##0.00;-#,##0.00");

    private final BotApi botApi;
    private final MeduzaStockProvider stockProvider;

    @Autowired
    public BrentCommand(BotApi botApi, MeduzaStockProvider stockProvider) {

        this.botApi = botApi;
        this.stockProvider = stockProvider;
    }

    @Override
    public boolean onUpdate(Update update) {


        MeduzaStockResponse stocks;
        Integer chatId = update.getMessage().getChat().getId();

        try {
            stocks = this.stockProvider.getCurrentStocks();
        } catch (Exception e) {
            log.error("Failed to retrieve stocks", e);
            botApi.sendMessage(chatId, "Meduza.io seems to be broken...");
            return true;
        }

        botApi.sendMessage(chatId, buildResponse(stocks), ParseMode.MARKDOWN, null, null, null);

        return true;
    }

    private String buildResponse(MeduzaStockResponse stocks) {

        float brentDelta = stocks.getBrent().getCurrent() - stocks.getBrent().getPrev();
        float usdDelta = stocks.getUsd().getCurrent() - stocks.getUsd().getPrev();
        float eurDelta = stocks.getEur().getCurrent() - stocks.getEur().getPrev();

        return line("Brent", stocks.getBrent().getCurrent(), brentDelta, DOLLAR_SYMBOL) +
                line("Usd", stocks.getUsd().getCurrent(), usdDelta, RUB_SYMBOL) +
                line("Eur", stocks.getEur().getCurrent(), eurDelta, RUB_SYMBOL);
    }

    private String line(String name, float value, float delta, char symbol) {

        String result = MessageFormat.format("*{0}*: {1} {2}", name, valueFormatter.format(value), symbol);

        if (delta != 0) {
            result += " (" + deltaFormatter.format(delta) + ")";
        }
        return result + "\n";
    }
}
