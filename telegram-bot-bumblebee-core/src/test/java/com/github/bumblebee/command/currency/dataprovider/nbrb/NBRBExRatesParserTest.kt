package com.github.bumblebee.command.currency.dataprovider.nbrb

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class NBRBExRatesParserTest {
    private val parser = NBRBExRatesParser()

    @Test
    fun `should parse all currencies`() {
        val dailyRates = parser.getDailyRates()
        assertCurrenciesPresent(dailyRates)
    }

    @Test
    fun `should parse all currencies by date`() {
        val dailyRates = parser.getDailyRates(LocalDate.now().minusYears(1))
        assertCurrenciesPresent(dailyRates)
    }

    private fun assertCurrenciesPresent(dailyRates: List<Currency>) {
        assertTrue(dailyRates.size >= 26)

        dailyRates.forEach { rate ->
            assertNotNull(rate.scale)
            assertNotNull(rate.code)
            assertNotNull(rate.name)
            assertNotNull(rate.rate)
            assertNotNull(rate.shortName)
        }
    }

}
