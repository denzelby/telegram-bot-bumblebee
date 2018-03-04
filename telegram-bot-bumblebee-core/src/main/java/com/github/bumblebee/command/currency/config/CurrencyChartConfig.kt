package com.github.bumblebee.command.currency.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "currency.chart")
class CurrencyChartConfig {
    class CurrencyCommandLocalization {
        var chartTitle: String? = null
        var chartDateAxis: String? = null
        var chartRateAxis: String? = null
    }

    lateinit var dateFormat: String
    val defaultCurrencies: List<String> = ArrayList()
    val localization = CurrencyCommandLocalization()
}
