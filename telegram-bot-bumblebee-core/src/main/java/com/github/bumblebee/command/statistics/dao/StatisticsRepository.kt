package com.github.bumblebee.command.statistics.dao

import com.github.bumblebee.command.statistics.entity.Statistic
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface StatisticsRepository : CrudRepository<Statistic, Long> {
    fun findStatisticByPostedDate(postedDate: LocalDate): List<Statistic>

    fun findStatisticByChatId(chatId: Long): List<Statistic>

    @Query("select s from Statistic s where chatId = ?1 and postedDate >= ?2 and postedDate <= ?3 " +
            "order by postedDate, messageCount desc")
    fun findStatisticsBetweenDateRange(chatId: Long, since: LocalDate, until: LocalDate): List<Statistic>
}