package com.github.bumblebee.command.currency.service

import com.github.bumblebee.command.currency.config.CurrencyChartConfig
import com.github.bumblebee.command.currency.domain.SupportedCurrency
import com.github.bumblebee.command.currency.domain.SupportedCurrency.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class ChartArgumentParserTest {

    @Mock
    private lateinit var config: CurrencyChartConfig

    @InjectMocks
    private lateinit var parser: ChartArgumentParser

    @Before
    fun init() {
        `when`(config.dateFormat).thenReturn("dd.MM.yyyy")
        `when`(config.defaultCurrencies).thenReturn(listOf(USD.name, EUR.name))
    }

    // Ranges

    @Test
    fun `should parse two ranges`() {
        testRange("01.02.2015 05.04.2015", LocalDate.of(2015, 2, 1), LocalDate.of(2015, 4, 5))
    }

    @Test
    fun `should parse single range`() {
        testRange("01.02.2015", LocalDate.of(2015, 2, 1), LocalDate.now())
    }

    @Test
    fun `should parse two ranges with mess`() {
        testRange("asdasd 01.02.2015 MESS 05.04.2015 44 zero", LocalDate.of(2015, 2, 1), LocalDate.of(2015, 4, 5))
    }

    @Test
    fun `should parse two reversed ranges with mess`() {
        testRange("asdasd      MESS 05.04.2015 44 zero 01.02.2015 2014", LocalDate.of(2015, 2, 1), LocalDate.of(2015, 4, 5))
    }

    @Test
    fun `should parse single range with mess`() {
        testRange("xyz 42 01.02.2015 bamboo", LocalDate.of(2015, 2, 1), LocalDate.now())
    }

    @Test
    fun `should parse single range with mess and year defined`() {
        testRange("xyz 42 01.02.2015 bamboo 2014", LocalDate.of(2015, 2, 1), LocalDate.now())
    }

    @Test
    fun `should parse to defaults`() {
        testRange("xyz 42 01.02 bamboo", currentMonthStart(), LocalDate.now())
    }

    @Test
    fun `should parse single future date to defaults`() {
        testRange("xyz 42 01.02.2050 bamboo", currentMonthStart(), LocalDate.now())
    }

    @Test
    fun `should parse future dates to current date`() {
        testRange("xyz 42 04.06.2014 01.02.2050 bamboo", LocalDate.of(2014, 6, 4), LocalDate.now())
    }

    @Test
    fun `should parse empty to defaults`() {
        testRange(null, currentMonthStart(), LocalDate.now())
        testRange("", currentMonthStart(), LocalDate.now())
    }

    @Test
    fun `should parse single year `() {
        testRange("2014", LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31))
    }

    @Test
    fun `should parse single year with mess`() {
        testRange("abc 42 2014 22 ", LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31))
    }

    @Test
    fun `should parse multiple years `() {
        testRange("2014 2016", LocalDate.of(2014, 1, 1), LocalDate.of(2016, 12, 31))
    }

    @Test
    fun `should parse multiple years with mess`() {
        testRange("rub sa 2014 usd 2016 2017 2001 22", LocalDate.of(2014, 1, 1), LocalDate.of(2016, 12, 31))
    }

    @Test
    fun `should parse concrete dates over years`() {
        testRange("2012 2013 01.02.2015 05.04.2015", LocalDate.of(2015, 2, 1), LocalDate.of(2015, 4, 5))
    }

    @Test
    fun `should parse to defaults not supported year`() {
        testRange("abc 42 1990 22 ", currentMonthStart(), LocalDate.now())
    }

    private fun testRange(argument: String?, expectedFrom: LocalDate, expectedTo: LocalDate) {
        //when
        val (from, to) = parser.getRange(argument)

        // then
        assertEquals(expectedFrom, from)
        assertEquals(expectedTo, to)
    }

    private fun currentMonthStart(): LocalDate {
        return LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1)
    }

    // Currencies

    @Test
    fun `should parse single currency`() {
        testCurrencies("usd", USD)
    }

    @Test
    fun `should parse multiple currencies`() {
        testCurrencies("EUR usd RuB", EUR, USD, RUB)
    }

    @Test
    fun `should parse multiple currencies with mess`() {
        testCurrencies("saz 2005 EUR 42 usd    bb 01.02.2016 RuB z", EUR, USD, RUB)
    }

    @Test
    fun `should parse to default currencies`() {
        testCurrencies(" acb 42", USD, EUR)
        testCurrencies("", USD, EUR)
        testCurrencies(null, USD, EUR)
    }

    private fun testCurrencies(argument: String?, vararg expectedCurrencies: SupportedCurrency) {
        val currencies = parser.getCurrencies(argument)

        assertEquals(expectedCurrencies.size.toLong(), currencies.size.toLong())
        for (i in currencies.indices) {
            assertEquals(expectedCurrencies[i].name, currencies[i])
        }
    }
}