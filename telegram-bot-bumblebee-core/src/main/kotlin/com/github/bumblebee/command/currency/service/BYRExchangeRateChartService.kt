package com.github.bumblebee.command.currency.service

import com.github.bumblebee.command.currency.config.CurrencyChartConfig
import com.github.bumblebee.command.currency.domain.DailyExchangeRate
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtilities
import org.jfree.chart.JFreeChart
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.category.CategoryDataset
import org.jfree.data.category.DefaultCategoryDataset
import org.springframework.stereotype.Service
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Service
class BYRExchangeRateChartService(private val currencyChartConfig: CurrencyChartConfig) {

    @Throws(IOException::class)
    fun createChartImage(rates: List<DailyExchangeRate>, detailed: Boolean, from: LocalDate, to: LocalDate): ByteArray {
        val sortedRates = rates.sortedBy { it.date }

        val chart = if (detailed)
            createBarChart(sortedRates)
        else
            createLineChart(sortedRates)
        val image = chart.createBufferedImage(getWidth(chart), DEFAULT_CHART_HEIGHT)
        return ChartUtilities.encodeAsPNG(image)
    }

    private fun createLineChart(rates: List<DailyExchangeRate>): JFreeChart {
        val localization = currencyChartConfig.localization
        val chart = ChartFactory.createLineChart(
                localization.chartTitle,
                localization.chartDateAxis,
                localization.chartRateAxis,
                createDataset(rates, true),
                PlotOrientation.VERTICAL, true, true, false)

        val minimumRate = getMinimumRate(rates)
        chart.categoryPlot.rangeAxis.lowerBound = minimumRate - minimumRate * 0.01

        return chart
    }

    private fun createBarChart(rates: List<DailyExchangeRate>): JFreeChart {
        val localization = currencyChartConfig.localization
        val chart = ChartFactory.createBarChart(
                localization.chartTitle,
                localization.chartDateAxis,
                localization.chartRateAxis,
                createDataset(rates, false),
                PlotOrientation.VERTICAL, true, true, false)

        val categoryPlot = chart.categoryPlot
        val renderer = categoryPlot.renderer
        // draw values on bars
        renderer.baseItemLabelGenerator = StandardCategoryItemLabelGenerator()
        renderer.setBaseItemLabelsVisible(true)
        categoryPlot.renderer = renderer

        // top margin
        val maxRate = getMaximumRate(rates)
        categoryPlot.rangeAxis.upperBound = maxRate + maxRate * MARGIN_PERCENT

        return chart
    }

    private fun createDataset(rates: List<DailyExchangeRate>, showMonth: Boolean): CategoryDataset {
        val data = DefaultCategoryDataset()
        val format = if (showMonth) "MMM yy" else "d.MM"
        val formatter = SimpleDateFormat(format, Locale.ENGLISH)

        rates.forEach { rate ->
            data.addValue(
                    rate.rate,
                    rate.currency,
                    formatter.format(rate.date)
            )
        }
        return data
    }

    private fun getMaximumRate(rates: List<DailyExchangeRate>): Double {
        return rates.maxBy { it.rate }?.rate ?: 0.0
    }

    private fun getMinimumRate(rates: List<DailyExchangeRate>): Double {
        return rates.minBy { it.rate }?.rate ?: 0.0
    }

    private fun getWidth(chart: JFreeChart): Int {
        val autoWidth = chart.categoryPlot.categories.size * CATEGORY_WIDTH
        return if (autoWidth > DEFAULT_CHART_WIDTH) autoWidth else DEFAULT_CHART_WIDTH
    }

    fun reduceToAverageByMonth(rates: List<DailyExchangeRate>): List<DailyExchangeRate> {
        val format = SimpleDateFormat("yyyy.MM")

        val grouping = rates.groupBy { it.currency }
                .mapValues { (_, currencyRates) -> currencyRates.groupBy { format.format(it.date) } }

        val reduced = ArrayList<DailyExchangeRate>()
        grouping.forEach { _, groupedRates ->
            groupedRates.values.forEach { monthRates ->
                val monthAvg = monthRates.map { it.rate }.average()
                val sampleRate = monthRates[0]
                reduced.add(DailyExchangeRate(sampleRate.date, sampleRate.currency, monthAvg))
            }
        }
        return reduced
    }

    companion object {
        private val MARGIN_PERCENT = 0.1
        private val DEFAULT_CHART_WIDTH = 600
        private val DEFAULT_CHART_HEIGHT = 500
        private val CATEGORY_WIDTH = 70
    }
}
