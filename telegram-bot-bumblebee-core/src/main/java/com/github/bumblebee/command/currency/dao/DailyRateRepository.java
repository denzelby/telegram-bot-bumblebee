package com.github.bumblebee.command.currency.dao;

import com.github.bumblebee.command.currency.domain.DailyExchangeRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DailyRateRepository extends CrudRepository<DailyExchangeRate, Long> {

    List<DailyExchangeRate> findByDateBetweenAndCurrencyIn(Date from, Date to, List<String> currencies);

}
