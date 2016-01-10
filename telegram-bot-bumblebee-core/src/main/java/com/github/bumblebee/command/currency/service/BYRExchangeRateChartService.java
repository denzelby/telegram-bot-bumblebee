package com.github.bumblebee.command.currency.service;

import com.github.bumblebee.command.currency.config.CurrencyChartConfig;
import com.github.bumblebee.command.currency.config.CurrencyCommandLocalization;
import com.github.bumblebee.command.currency.domain.DailyExchangeRate;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BYRExchangeRateChartService {

    private static final double MARGIN_PERCENT = 0.1d;
    private static final int DEFAULT_CHART_WIDTH = 600;
    private static final int DEFAULT_CHART_HEIGHT = 500;
    private static final int CATEGORY_WIDTH = 70;

    private final CurrencyChartConfig currencyChartConfig;

    @Autowired
    public BYRExchangeRateChartService(CurrencyChartConfig currencyChartConfig) {
        this.currencyChartConfig = currencyChartConfig;
    }

    public byte[] createChartImage(List<DailyExchangeRate> rates, boolean detailed, LocalDate from, LocalDate to) throws IOException {

        rates.sort(Comparator.comparing(DailyExchangeRate::getDate));

        JFreeChart chart;
        if (detailed) {
            chart = createBarChart(rates);
        } else {
            chart = createLineChart(rates);
        }
        BufferedImage image = chart.createBufferedImage(getWidth(chart), DEFAULT_CHART_HEIGHT);
        return ChartUtilities.encodeAsPNG(image);
    }

    private JFreeChart createLineChart(List<DailyExchangeRate> rates) {
        CurrencyCommandLocalization localization = currencyChartConfig.getLocalization();
        JFreeChart chart = ChartFactory.createLineChart(
                localization.getChartTitle(),
                localization.getChartDateAxis(),
                localization.getChartRateAxis(),
                createDataset(rates, true),
                PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        double minimumRate = getMinimumRate(rates);
        categoryPlot.getRangeAxis().setLowerBound(minimumRate - minimumRate * 0.01);

        return chart;
    }

    private JFreeChart createBarChart(List<DailyExchangeRate> rates) {
        CurrencyCommandLocalization localization = currencyChartConfig.getLocalization();
        JFreeChart chart = ChartFactory.createBarChart(
                localization.getChartTitle(),
                localization.getChartDateAxis(),
                localization.getChartRateAxis(),
                createDataset(rates, false),
                PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        CategoryItemRenderer renderer = categoryPlot.getRenderer();
        // draw values on bars
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        categoryPlot.setRenderer(renderer);

        // top margin
        double maxRate = getMaximumRate(rates);
        categoryPlot.getRangeAxis().setUpperBound(maxRate + maxRate * MARGIN_PERCENT);

        return chart;
    }

    private CategoryDataset createDataset(List<DailyExchangeRate> rates, boolean showMonth) {
        final DefaultCategoryDataset data = new DefaultCategoryDataset();
        final String format = (showMonth) ? "MMM yy" : "d.MM";
        final SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);

        rates.forEach(rate -> data.addValue(
                rate.getRate(),
                rate.getCurrency(),
                formatter.format(rate.getDate())
        ));
        return data;
    }

    private double getMaximumRate(List<DailyExchangeRate> rates) {
        return rates.stream()
                .mapToDouble(DailyExchangeRate::getRate)
                .max()
                .getAsDouble();
    }

    private double getMinimumRate(List<DailyExchangeRate> rates) {
        return rates.stream()
                .mapToDouble(DailyExchangeRate::getRate)
                .min()
                .getAsDouble();
    }

    private int getWidth(JFreeChart chart) {
        int autoWidth = chart.getCategoryPlot().getCategories().size() * CATEGORY_WIDTH;
        return (autoWidth > DEFAULT_CHART_WIDTH) ? autoWidth : DEFAULT_CHART_WIDTH;
    }

    public List<DailyExchangeRate> reduceToAverageByMonth(List<DailyExchangeRate> rates) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");

        Map<String, Map<String, List<DailyExchangeRate>>> grouping = rates.stream()
                .collect(Collectors.groupingBy(DailyExchangeRate::getCurrency,
                        Collectors.groupingBy(rate -> format.format(rate.getDate()))));

        List<DailyExchangeRate> reduced = new ArrayList<>();
        grouping.forEach((currency, groupedRates) ->
                groupedRates.values().forEach(monthRates -> {
                    double monthAvg = monthRates.stream()
                            .mapToDouble(DailyExchangeRate::getRate)
                            .average()
                            .getAsDouble();
                    DailyExchangeRate sampleRate = monthRates.get(0);
                    reduced.add(new DailyExchangeRate(sampleRate.getDate(), sampleRate.getCurrency(), monthAvg));
                })
        );
        return reduced;
    }
}
