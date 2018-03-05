package com.github.bumblebee.command.statistics.service

import com.github.bumblebee.command.statistics.dao.StatisticsRepository
import com.github.bumblebee.command.statistics.entity.Statistic
import org.springframework.stereotype.Service
import java.lang.StringBuilder
import java.time.LocalDate

@Service
class StatisticsService(private val repository: StatisticsRepository) {

    private val existingStatistics: Map<Long, List<Statistic>> = fillStatisticsMap()

    private fun getAllUserStatisticsForCurrentDayAndChat(chatId: Long): List<Statistic>? {
        return existingStatistics[chatId]
                ?.filter { it.postedDate == LocalDate.now() }
    }

    private fun fillStatisticsMap (): Map<Long, List<Statistic>> {
        return getAllUserStatisticsForCurrentDay().groupBy { it.chatId }
    }

    fun getChatsWithStatistic (): Set<Long> {
        return existingStatistics.keys
    }

    private fun getAllUserStatisticsForCurrentDay(): List<Statistic> {
        return repository.findStatisticByPostedDate(LocalDate.now()).orEmpty()
    }

    private fun getSortedStatisticsByMessagesCountInChat(chatId: Long): List<Statistic>? {
        return getAllUserStatisticsForCurrentDayAndChat(chatId)?.sortedBy { statistic -> statistic.messageCount }
    }

    private fun getUserStatisticForCurrentDayInChat(userId: Long, chatId: Long): Statistic? {
        return repository.findStatisticByUserIdAndPostedDateAndChatId(userId, LocalDate.now(), chatId)
    }

    fun saveOrUpdateUserStatistic(userId: Long, chatId: Long, userName: String?) {
        val existingStat = getUserStatisticForCurrentDayInChat(userId, chatId)
        if (existingStat == null) {

        }
    }

    fun buildStatisticsForCurrentDayInChat(chatId: Long): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Day message statistics:").append("\n")
        val statistics = getSortedStatisticsByMessagesCountInChat(chatId)
        if (statistics != null) {
            statistics.forEach {
                stringBuilder.append(it.authorName)
                        .append(": ")
                        .append(it.messageCount)
                        .append("\n")
            }
            return stringBuilder.toString()
        } else return "Statistics is empty for today"
    }


}