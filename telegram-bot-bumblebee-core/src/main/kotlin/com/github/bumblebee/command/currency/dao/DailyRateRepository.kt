package com.github.bumblebee.command.currency.dao

import com.github.bumblebee.command.currency.domain.DailyExchangeRate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DailyRateRepository : CrudRepository<DailyExchangeRate, Long> {
    fun findByDateBetweenAndCurrencyIn(from: Date, to: Date, currencies: List<String>): List<DailyExchangeRate>
}
