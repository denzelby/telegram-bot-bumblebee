package com.github.bumblebee.command.statistics.dao

import com.github.bumblebee.command.statistics.entity.Statistic
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface StatisticsRepository : CrudRepository<Statistic, Long> {

    fun findStatisticByPostedDateAndChatId(postedDate: LocalDate, chatId: Long): List<Statistic>?

    fun findStatisticByPostedDate(postedDate: LocalDate): List<Statistic>?

    fun findStatisticByAuthorIdAndPostedDateAndChatId (authorId: Long, postedDate: LocalDate, chatId: Long): Statistic?
}