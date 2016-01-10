package com.github.bumblebee.command.currency.service;

import com.github.bumblebee.command.currency.config.CurrencyChartConfig;
import com.github.bumblebee.command.currency.domain.SupportedCurrency;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;

import static com.github.bumblebee.command.currency.domain.SupportedCurrency.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChartArgumentParserTest {

    @Mock
    private CurrencyChartConfig config;

    @InjectMocks
    private ChartArgumentParser parser;

    @Before
    public void init() throws Exception {
        when(config.getDateFormat()).thenReturn("dd.MM.yyyy");
        when(config.getDefaultCurrencies()).thenReturn(
                Lists.newArrayList(USD.name(), EUR.name()));
    }

    // Ranges

    @Test
    public void shouldParseTwoRanges() {
        testRange("01.02.2015 05.04.2015", LocalDate.of(2015, 2, 1), LocalDate.of(2015, 4, 5));
    }

    @Test
    public void shouldParseSingleRange() {
        testRange("01.02.2015", LocalDate.of(2015, 2, 1), LocalDate.now());
    }

    @Test
    public void shouldParseTwoRangesWithMess() {
        testRange("asdasd 01.02.2015 MESS 05.04.2015 44 zero", LocalDate.of(2015, 2, 1), LocalDate.of(2015, 4, 5));
    }

    @Test
    public void shouldParseTwoReversedRangesWithMess() {
        testRange("asdasd      MESS 05.04.2015 44 zero 01.02.2015 2014", LocalDate.of(2015, 2, 1), LocalDate.of(2015, 4, 5));
    }

    @Test
    public void shouldParseSingleRangeWithMess() {
        testRange("xyz 42 01.02.2015 bamboo", LocalDate.of(2015, 2, 1), LocalDate.now());
    }

    @Test
    public void shouldParseSingleRangeWithMessAndYearDefined() {
        testRange("xyz 42 01.02.2015 bamboo 2014", LocalDate.of(2015, 2, 1), LocalDate.now());
    }

    @Test
    public void shouldParseToDefaults() {
        testRange("xyz 42 01.02 bamboo", currentMonthStart(), LocalDate.now());
    }

    @Test
    public void shouldParseSingleFutureDateToDefaults() {
        testRange("xyz 42 01.02.2018 bamboo", currentMonthStart(), LocalDate.now());
    }

    @Test
    public void shouldParseFutureDatesToCurrentDate() {
        testRange("xyz 42 04.06.2014 01.02.2018 bamboo", LocalDate.of(2014, 6, 4), LocalDate.now());
    }

    @Test
    public void shouldParseEmptyToDefaults() {
        testRange(null, currentMonthStart(), LocalDate.now());
        testRange("", currentMonthStart(), LocalDate.now());
    }

    @Test
    public void shouldParseSingleYear() {
        testRange("2014", LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
    }

    @Test
    public void shouldParseSingleYearWithMess() {
        testRange("abc 42 2014 22 ", LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
    }

    @Test
    public void shouldParseMultipleYears() {
        testRange("2014 2016", LocalDate.of(2014, 1, 1), LocalDate.of(2016, 12, 31));
    }

    @Test
    public void shouldParseMultipleYearsWithMess() {
        testRange("rub sa 2014 usd 2016 2017 2001 22", LocalDate.of(2014, 1, 1), LocalDate.of(2016, 12, 31));
    }

    @Test
    public void shouldParseConcreteDatesOverYears() {
        testRange("2012 2013 01.02.2015 05.04.2015", LocalDate.of(2015, 2, 1), LocalDate.of(2015, 4, 5));
    }

    @Test
    public void shouldParseToDefaultsNotSupportedYear() {
        testRange("abc 42 1990 22 ", currentMonthStart(), LocalDate.now());
    }

    private void testRange(String argument, LocalDate expectedFrom, LocalDate expectedTo) {
        //when
        ChartArgumentParser.DateRange range = parser.getRange(argument);

        // then
        assertEquals(expectedFrom, range.getFrom());
        assertEquals(expectedTo, range.getTo());
    }

    private LocalDate currentMonthStart() {
        return LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
    }

    // Currencies

    @Test
    public void shouldParseSingleCurrency() {
        testCurrencies("usd", USD);
    }

    @Test
    public void shouldParseMultipleCurrencies() {
        testCurrencies("EUR usd RuB", EUR, USD, RUB);
    }

    @Test
    public void shouldParseMultipleCurrenciesWithMess() {
        testCurrencies("saz 2005 EUR 42 usd    bb 01.02.2016 RuB z", EUR, USD, RUB);
    }

    @Test
    public void shouldParseToDefaultCurrencies() {
        testCurrencies(" acb 42", USD, EUR);
        testCurrencies("", USD, EUR);
        testCurrencies(null, USD, EUR);
    }

    private void testCurrencies(String argument, SupportedCurrency... expectedCurrencies) {
        List<String> currencies = parser.getCurrencies(argument);

        assertEquals(expectedCurrencies.length, currencies.size());
        for (int i = 0; i < currencies.size(); i++) {
            assertEquals(expectedCurrencies[i].name(), currencies.get(i));
        }
    }
}