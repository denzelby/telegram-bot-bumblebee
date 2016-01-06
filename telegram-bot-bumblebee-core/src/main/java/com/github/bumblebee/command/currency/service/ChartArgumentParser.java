package com.github.bumblebee.command.currency.service;

import com.github.bumblebee.command.currency.config.CurrencyChartConfig;
import com.github.bumblebee.command.currency.domain.SupportedCurrency;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class ChartArgumentParser {

    private static final int MIN_SUPPORTED_YEAR = 2000;
    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private final CurrencyChartConfig chartConfig;

    public class DateRange {
        private final LocalDate from;
        private final LocalDate to;

        public DateRange(LocalDate from, LocalDate to) {
            this.from = from;
            this.to = to;
        }

        public LocalDate getFrom() {
            return from;
        }

        public LocalDate getTo() {
            return to;
        }
    }

    @Autowired
    public ChartArgumentParser(CurrencyChartConfig chartConfig) {
        this.chartConfig = chartConfig;
    }

    public List<String> getCurrencies(String argument) {

        if (StringUtils.isEmpty(argument)) {
            return chartConfig.getDefaultCurrencies();
        }

        StringTokenizer tokenizer = new StringTokenizer(argument, " ");
        ArrayList<String> result = Lists.newArrayListWithExpectedSize(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            SupportedCurrency supportedCurrency = SupportedCurrency.parse(tokenizer.nextToken());
            if (supportedCurrency != null) {
                result.add(supportedCurrency.name());
            }
        }
        return (result.isEmpty()) ? chartConfig.getDefaultCurrencies() : result;
    }

    public DateRange getRange(String argument) {

        if (StringUtils.isEmpty(argument)) {
            return defaultRange();
        }

        StringTokenizer tokenizer = new StringTokenizer(argument, " ");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(chartConfig.getDateFormat());
        LocalDate from = null;
        LocalDate to = null;
        Integer yearFrom = null;
        Integer yearTo = null;

        final LocalDate now = LocalDate.now();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            try {
                LocalDate date = LocalDate.parse(token, dateFormatter);
                if (date.isAfter(now)) {
                    continue;
                }
                if (from == null) {
                    from = date;
                } else {
                    to = date;
                    break;
                }
            } catch (DateTimeParseException e) {
                if (yearFrom == null) {
                    yearFrom = tryParseAsYear(token);
                } else if (yearTo == null) {
                    yearTo = tryParseAsYear(token);
                }
            }
        }

        // concrete date range
        if (from != null) {
            return rangeFrom(from, Optional.fromNullable(to).or(now));
        }

        // year range
        if (yearFrom != null) {
            return new DateRange(
                    LocalDate.of(yearFrom, 1, 1),
                    LocalDate.of(Optional.fromNullable(yearTo).or(yearFrom), 12, 31));
        }

        // no dates passed
        return defaultRange();
    }

    private Integer tryParseAsYear(String token) {
        try {
            int number = Integer.parseUnsignedInt(token, 10);
            return (number >= MIN_SUPPORTED_YEAR && number <= CURRENT_YEAR) ? number : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private DateRange rangeFrom(LocalDate date1, LocalDate date2) {
        if (date2.isBefore(date1)) {
            return new DateRange(date2, date1);
        }
        return new DateRange(date1, date2);
    }

    private DateRange defaultRange() {
        LocalDate to = LocalDate.now();
        LocalDate from = LocalDate.of(to.getYear(), to.getMonth(), 1);
        return new DateRange(from, to);
    }
}
