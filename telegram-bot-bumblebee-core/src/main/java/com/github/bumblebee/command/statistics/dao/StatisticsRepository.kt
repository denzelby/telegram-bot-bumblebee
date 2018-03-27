package com.github.bumblebee.command.statistics.dao

import com.github.bumblebee.command.statistics.entity.Statistic
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface StatisticsRepository : CrudRepository<Statistic, Long> {
    fun findStatisticByPostedDate(postedDate: LocalDate): List<Statistic>

    fun findStatisticByChatId(chatId: Long): List<Statistic>
}