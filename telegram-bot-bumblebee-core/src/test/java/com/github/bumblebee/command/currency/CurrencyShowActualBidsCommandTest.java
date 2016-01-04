package com.github.bumblebee.command.currency;

import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

public class CurrencyShowActualBidsCommandTest {

    @Test
    public void testNumberFormat() throws Exception {

        DecimalFormat fmt = new DecimalFormat("+#,##0;-#");

        assertEquals("+5", fmt.format(5));
        assertEquals("+0", fmt.format(0));
        assertEquals("-2", fmt.format(-2));
    }
}