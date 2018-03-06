package com.github.bumblebee.command.statistics.service

import com.github.bumblebee.command.statistics.dao.StatisticsRepository
import com.github.bumblebee.command.statistics.entity.Statistic
import org.springframework.stereotype.Service
import java.lang.StringBuilder
import java.time.LocalDate

@Service
class StatisticsService(private val repository: StatisticsRepository) {

    private val existingStatistics: MutableMap<Long, MutableList<Statistic>> = fillStatisticsMap()

    private fun getAllUserStatisticsForCurrentDayAndChat(chatId: Long): MutableList<Statistic>? {
        return existingStatistics[chatId]
                ?.filterTo(mutableListOf()) { it.postedDate == LocalDate.now() }
    }

    private fun fillStatisticsMap(): MutableMap<Long, MutableList<Statistic>> {
        return getAllUserStatisticsForCurrentDay().groupByTo(mutableMapOf()) { it.chatId }
    }

    fun getChatsWithStatistic(): Set<Long> {
        return existingStatistics.keys
    }

    private fun getAllUserStatisticsForCurrentDay(): MutableList<Statistic> {
        return repository.findStatisticByPostedDate(LocalDate.now()).orEmpty().toMutableList()
    }

    private fun getSortedStatisticsByMessagesCountInChat(chatId: Long): List<Statistic>? {
        return getAllUserStatisticsForCurrentDayAndChat(chatId)?.sortedBy { statistic -> statistic.messageCount }
    }

    private fun getUserStatisticForCurrentDayInChat(userId: Long, chatId: Long): Statistic? {
        return getSortedStatisticsByMessagesCountInChat(chatId)?.find { it.authorId == userId }
    }

    fun saveOrUpdateUserStatistic(userId: Long, chatId: Long, userName: String?) {
        val existingStat = getAllUserStatisticsForCurrentDayAndChat(chatId)
        if (existingStat == null) {
            val userStat = buildUserStat(userId, chatId, userName)
            existingStatistics[chatId] = mutableListOf(userStat)
            repository.save(userStat)
        } else {
            var userStat = existingStat.find { it.authorId == userId }
            if (userStat != null) {
                userStat.messageCount += 1
            } else {
                userStat = buildUserStat(userId, chatId, userName)
                existingStatistics[chatId]?.add(userStat)
            }
            repository.save(userStat)
        }
    }

    private fun buildUserStat(userId: Long, chatId: Long, userName: String?): Statistic {
        return Statistic(LocalDate.now(), Integer.valueOf(1), chatId, userId, userName)
    }

    fun buildStatisticsForCurrentDayInChat(chatId: Long): String {
        return getSortedStatisticsByMessagesCountInChat(chatId)?.joinToString(separator = "\n",
                prefix = "Day message statistics:\n",
                transform = { statistic -> statistic.authorName + ": " + statistic.messageCount })
                ?: "Statistics is empty for today"
    }

    fun buildDayStatisticForUserInChat(chatId: Long, userId: Long): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Your message statistics for this day:").append("\n")
        val statistic = getUserStatisticForCurrentDayInChat(userId, chatId)
        return if (statistic != null) {
            stringBuilder.append(statistic.authorName)
                    .append(": ")
                    .append(statistic.messageCount)
            stringBuilder.toString()
        } else "Your statistic is empty for today"
    }


}