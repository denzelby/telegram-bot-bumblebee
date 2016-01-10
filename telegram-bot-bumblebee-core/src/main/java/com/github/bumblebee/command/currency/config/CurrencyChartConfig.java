package com.github.bumblebee.command.currency.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "currency.chart")
public class CurrencyChartConfig {

    private List<String> defaultCurrencies;
    private String dateFormat;
    private CurrencyCommandLocalization localization;

    public List<String> getDefaultCurrencies() {
        return defaultCurrencies;
    }

    public void setDefaultCurrencies(List<String> defaultCurrencies) {
        this.defaultCurrencies = defaultCurrencies;
    }

    public CurrencyCommandLocalization getLocalization() {
        return localization;
    }

    public void setLocalization(CurrencyCommandLocalization localization) {
        this.localization = localization;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
