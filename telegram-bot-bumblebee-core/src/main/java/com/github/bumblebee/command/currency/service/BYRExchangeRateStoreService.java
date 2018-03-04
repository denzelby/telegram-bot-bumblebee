package com.github.bumblebee.command.currency.service;

import com.github.bumblebee.command.currency.dao.DailyRateRepository;
import com.github.bumblebee.command.currency.dataprovider.nbrb.Currency;
import com.github.bumblebee.command.currency.dataprovider.nbrb.NBRBExRatesParser;
import com.github.bumblebee.command.currency.domain.DailyExchangeRate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BYRExchangeRateStoreService {

    private static final Logger log = LoggerFactory.getLogger(BYRExchangeRateStoreService.class);

    private final NBRBExRatesParser parser;
    private final DailyRateRepository repository;

    @Autowired
    public BYRExchangeRateStoreService(NBRBExRatesParser parser, DailyRateRepository repository) {
        this.parser = parser;
        this.repository = repository;
    }

    public List<DailyExchangeRate> getRates(LocalDate from, LocalDate to, List<String> currencies) throws IOException, SAXException {

        Assert.isTrue(from.isBefore(to) || from.isEqual(to), "'to' cannot be after 'from' date");

        List<DailyExchangeRate> dbRates = repository
                .findByDateBetweenAndCurrencyIn(toDate(from), toDate(to), currencies);

        Set<LocalDate> dateRange = createDateRange(from, to);

        Set<LocalDate> dbRatesDates = dbRates.stream()
                .map(rate -> toLocalDate(rate.getDate()))
                .collect(Collectors.toSet());

        Sets.SetView<LocalDate> ratesToFetch = Sets.difference(dateRange, dbRatesDates);

        if (ratesToFetch.size() > 0) {
            log.info("Rates need to be fetched", ratesToFetch.size());
            if (log.isTraceEnabled()) {
                ratesToFetch.forEach(rate -> log.trace(rate.toString()));
            }

            List<DailyExchangeRate> fetchedRates = fetchRates(ratesToFetch);
            return mergeRates(dbRates, fetchedRates, currencies);
        }
        return dbRates;
    }

    private List<DailyExchangeRate> mergeRates(List<DailyExchangeRate> dbRates, List<DailyExchangeRate> fetchedRates,
                                               List<String> currencies) {

        Set<String> currencySet = new HashSet<>(currencies);

        List<DailyExchangeRate> filteredRates = fetchedRates.stream()
                .filter(rate -> currencySet.contains(rate.getCurrency()))
                .collect(Collectors.toList());

        List<DailyExchangeRate> result = Lists.newArrayListWithExpectedSize(dbRates.size() + filteredRates.size());
        result.addAll(dbRates);
        result.addAll(filteredRates);
        return result;
    }

    private List<DailyExchangeRate> fetchRates(Set<LocalDate> ratesToFetch) throws IOException, SAXException {

        List<DailyExchangeRate> result = new ArrayList<>();

        for (LocalDate dateToFetch : ratesToFetch) {
            List<Currency> fetchedRates = parser.getDailyRates(dateToFetch);
            List<DailyExchangeRate> storedRates = storeRates(fetchedRates, dateToFetch);

            result.addAll(storedRates);
        }
        return result;
    }

    private List<DailyExchangeRate> storeRates(List<Currency> rates, LocalDate localDate) {

        final Date date = toDate(localDate);

        List<DailyExchangeRate> ratesToSave = rates.stream()
                .map(currency -> new DailyExchangeRate(date, currency.getShortName(), currency.getRate()))
                .collect(Collectors.toList());

        repository.saveAll(ratesToSave);
        return ratesToSave;
    }

    private Set<LocalDate> createDateRange(LocalDate from, LocalDate to) {
        Set<LocalDate> range = new HashSet<>();
        LocalDate cursor = from;

        while (cursor.isBefore(to) || cursor.isEqual(to)) {
            range.add(cursor);
            cursor = cursor.plusDays(1);
        }
        return range;
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
