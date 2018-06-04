package com.github.bumblebee.command.statistics.service

import com.github.bumblebee.command.statistics.dao.StatisticsRepository
import com.github.bumblebee.command.statistics.entity.Statistic
import com.github.bumblebee.util.logger
import org.jfree.chart.*
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.category.BarRenderer
import org.jfree.chart.renderer.category.StandardBarPainter
import org.jfree.chart.ui.RectangleInsets
import org.jfree.data.category.DefaultCategoryDataset
import org.springframework.stereotype.Service
import java.awt.BasicStroke
import java.awt.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class StatisticsChartService(private val repository: StatisticsRepository) {

    fun getWeeklyStats(chatId: Long, since: LocalDate, until: LocalDate): Optional<ByteArray> {
        log.info("Weekly stats: {} to {}", since, until)
        val stats = repository.findStatisticsBetweenDateRange(chatId, since, until)

        return if (stats.isNotEmpty()) {
            val renderStart = System.currentTimeMillis()
            val image = render(createChart(stats, since, until))
            log.info("Rendered chart in {}ms", System.currentTimeMillis() - renderStart)
            return Optional.of(image)
        } else Optional.empty()
    }

    private fun render(chart: JFreeChart): ByteArray {
        val image = chart.createBufferedImage(640, 480)
        return ChartUtils.encodeAsPNG(image)
    }

    private fun createChart(stats: List<Statistic>, weekStart: LocalDate, weekEnd: LocalDate): JFreeChart {
        val title = "${weekStart.format(titleDateFormat)} - ${weekEnd.format(titleDateFormat)}"
        val chart = ChartFactory.createStackedBarChart(
            title, null, null,
            createDataset(stats), PlotOrientation.VERTICAL, true, false, false
        )
        applyStyles(chart)
        return chart
    }

    private fun applyStyles(chart: JFreeChart) {
        theme.apply(chart)
        chart.categoryPlot.apply {
            isOutlineVisible = false
            rangeAxis.isAxisLineVisible = false
            rangeAxis.isTickMarksVisible = false
            rangeGridlineStroke = BasicStroke()
            rangeAxis.tickLabelPaint = Color.decode("#666666")
            domainAxis.tickLabelPaint = Color.decode("#666666")
            rowColors.forEachIndexed { index, color -> renderer.setSeriesPaint(index, color) }
            (renderer as BarRenderer).apply {
                setShadowVisible(true)
                shadowXOffset = 1.0
                shadowYOffset = 1.0
                shadowPaint = Color.decode("#878787")
                maximumBarWidth = 0.1
            }
        }
        chart.setTextAntiAlias(true)
        chart.antiAlias = true
    }

    private fun createDataset(stats: List<Statistic>) = DefaultCategoryDataset().apply {
        stats.forEach {
            addValue(it.messageCount, it.authorName, it.postedDate?.format(dayFormat))
        }
    }

    companion object {
        private val log = logger<StatisticsChartService>()
        private val lang = Locale.forLanguageTag("RU")
        private val dayFormat = DateTimeFormatter.ofPattern("EE", lang)
        private val titleDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy", lang)
        private val theme = chartTheme()
        private val rowColors = arrayOf(
            Color.decode("#673AB7"),
            Color.decode("#B39DDB"),
            Color.decode("#0D47A1"),
            Color.decode("#2196F3"),
            Color.decode("#90CAF9"),
            Color.decode("#00695C"),
            Color.decode("#009688"),
            Color.decode("#80CBC4"),
            Color.decode("#CDDC39"),
            Color.decode("#E6EE9C"),
            Color.decode("#9C27B0"),
            Color.decode("#CE93D8"),
            Color.decode("#F44336"),
            Color.decode("#EF9A9A"),
            Color.decode("#FFEB3B"),
            Color.decode("#FFC107"),
            Color.decode("#FF9800"),
            Color.decode("#FF5722"),
            Color.decode("#795548"),
            Color.decode("#9E9E9E"),
            Color.decode("#607D8B")
        )

        private fun chartTheme(): ChartTheme {
            return (StandardChartTheme.createJFreeTheme() as StandardChartTheme).apply {
                titlePaint = Color.decode("#4572a7")
                rangeGridlinePaint = Color.decode("#C0C0C0")
                plotBackgroundPaint = Color.white
                chartBackgroundPaint = Color.white
                gridBandPaint = Color.red
                axisOffset = RectangleInsets(0.0, 0.0, 0.0, 0.0)
                barPainter = StandardBarPainter()
                axisLabelPaint = Color.decode("#666666")
            }
        }
    }
}
