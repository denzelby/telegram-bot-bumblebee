package com.github.bumblebee.command.currency.dataprovider.nbrb;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NBRBExRatesParserTest {

    private NBRBExRatesParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new NBRBExRatesParser();
    }

    @Test
    public void testParseAllCurrencies() throws Exception {

        List<Currency> dailyRates = parser.getDailyRates();
        assertCurrenciesPresent(dailyRates);
    }

    @Test
    public void testParseAllCurrenciesByDate() throws Exception {

        List<Currency> dailyRates = parser.getDailyRates(LocalDate.now().minusYears(1));
        assertCurrenciesPresent(dailyRates);
    }

    private void assertCurrenciesPresent(List<Currency> dailyRates) {
        assertTrue(dailyRates.size() >= 26);

        dailyRates.forEach(rate -> {
            assertNotNull(rate.getScale());
            assertNotNull(rate.getCode());
            assertNotNull(rate.getName());
            assertNotNull(rate.getRate());
            assertNotNull(rate.getShortName());
        });
    }

}