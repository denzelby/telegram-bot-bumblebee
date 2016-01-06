package com.github.bumblebee.command.currency;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.currency.domain.DailyExchangeRate;
import com.github.bumblebee.command.currency.service.BYRExchangeRateChartService;
import com.github.bumblebee.command.currency.service.BYRExchangeRateStoreService;
import com.github.bumblebee.command.currency.service.ChartArgumentParser;
import com.github.bumblebee.service.RandomPhraseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import telegram.api.BotApi;
import telegram.domain.Update;
import telegram.domain.request.InputFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
public class CurrencyChartCommand extends SingleArgumentCommand {

    private static final Logger log = LoggerFactory.getLogger(CurrencyChartCommand.class);
    
    private static final int BAR_TO_LINE_CHART_THRESHOLD_DAYS = 40;
    private static final int REDUCE_THRESHOLD_DAYS = 5 * 30;

    protected final BotApi botApi;
    protected final RandomPhraseService randomPhrase;
    protected final BYRExchangeRateStoreService exchangeRateStoreService;
    protected final BYRExchangeRateChartService chartService;
    protected final ChartArgumentParser argumentParser;

    @Autowired
    public CurrencyChartCommand(BotApi botApi,
                                RandomPhraseService randomPhrase,
                                BYRExchangeRateStoreService exchangeRateStoreService,
                                BYRExchangeRateChartService chartService,
                                ChartArgumentParser argumentParser) {
        this.botApi = botApi;
        this.randomPhrase = randomPhrase;
        this.exchangeRateStoreService = exchangeRateStoreService;
        this.chartService = chartService;
        this.argumentParser = argumentParser;
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {

        ChartArgumentParser.DateRange range = argumentParser.getRange(argument);
        String errorMessage = validateRange(range.getFrom(), range.getTo());
        if (errorMessage != null) {
            botApi.sendMessage(chatId, errorMessage, update.getMessage().getMessageId());
            return;
        }
        List<String> currencies = argumentParser.getCurrencies(argument);

        buildChart(update, range.getFrom(), range.getTo(), currencies);
    }

    private String validateRange(LocalDate from, LocalDate to) {
        Period period = Period.between(from, to);
        if (period.getYears() > 3) {
            return "Period too large";
        }
        return null;
    }

    private void buildChart(Update update, LocalDate from, LocalDate to, List<String> currencies) {

        final Integer chatId = update.getMessage().getChat().getId();

        Period period = Period.between(from, to);
        long approxDays = period.toTotalMonths() * 30 + period.getDays();
        boolean detailed = approxDays < BAR_TO_LINE_CHART_THRESHOLD_DAYS;
        boolean reduce = approxDays > REDUCE_THRESHOLD_DAYS;

        try {
            List<DailyExchangeRate> rates = exchangeRateStoreService.getRates(from, to, currencies);
            log.info("Fetched {} exchange rate records", rates.size());
            if (reduce) {
                rates = chartService.reduceToAverageByMonth(rates);
                log.info("{} records after reduction", rates.size());
            }

            byte[] png = chartService.createChartImage(rates, detailed, from, to);
            InputFile chart = InputFile.photo(new ByteArrayInputStream(png), "chart.png");

            botApi.sendPhoto(chatId.toString(), chart);
        } catch (IOException | SAXException e) {
            log.error("Chart creation failed", e);
            botApi.sendMessage(chatId, randomPhrase.no(), update.getMessage().getMessageId());
        }
    }

}
