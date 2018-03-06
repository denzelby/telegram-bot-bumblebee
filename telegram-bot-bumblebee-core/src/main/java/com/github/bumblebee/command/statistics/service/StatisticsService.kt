package com.github.bumblebee.command.statistics.service

import com.github.bumblebee.command.statistics.dao.StatisticsRepository
import com.github.bumblebee.command.statistics.entity.Statistic
import org.springframework.stereotype.Service
import java.lang.StringBuilder
import java.time.LocalDate

@Service
class StatisticsService(private val repository: StatisticsRepository) {

    private val existingStatistics: MutableMap<Long, List<Statistic>> = fillStatisticsMap()

    private fun getAllUserStatisticsForCurrentDayAndChat(chatId: Long): List<Statistic>? {
        return existingStatistics[chatId]
                ?.filter { it.postedDate == LocalDate.now() }
    }

    private fun fillStatisticsMap(): MutableMap<Long, List<Statistic>> {
        return getAllUserStatisticsForCurrentDay().groupBy { it.chatId }.toMutableMap()
    }

    fun getChatsWithStatistic(): Set<Long> {
        return existingStatistics.keys
    }

    private fun getAllUserStatisticsForCurrentDay(): List<Statistic> {
        return repository.findStatisticByPostedDate(LocalDate.now()).orEmpty()
    }

    private fun getSortedStatisticsByMessagesCountInChat(chatId: Long): List<Statistic>? {
        return getAllUserStatisticsForCurrentDayAndChat(chatId)?.sortedBy { statistic -> statistic.messageCount }
    }

    private fun getUserStatisticForCurrentDayInChat(userId: Long, chatId: Long): Statistic? {
        //return repository.findStatisticByUserIdAndPostedDateAndChatId(userId, LocalDate.now(), chatId)
        val allStatForChat = getSortedStatisticsByMessagesCountInChat(chatId)
        if(allStatForChat != null) {
            return allStatForChat.find{ it.authorId == userId }
        } else return null
    }

    fun saveOrUpdateUserStatistic(userId: Long, chatId: Long, userName: String?) {
        val existingStat = getAllUserStatisticsForCurrentDayAndChat(chatId)
        if (existingStat == null) {
            val userStat = buildUserStat(userId, chatId, userName)
            existingStatistics[chatId] = listOf(userStat)
            repository.save(userStat)
        } else {
            var userStat = existingStat.find { it.authorId == userId }
            if (userStat != null) {
                userStat.messageCount += 1
            } else {
                userStat = buildUserStat(userId, chatId, userName)
            }
            repository.save(userStat)
        }
    }

    private fun buildUserStat(userId: Long, chatId: Long, userName: String?): Statistic {
        val statistic = Statistic()
        statistic.chatId = chatId
        statistic.authorId = userId
        statistic.messageCount = Integer.valueOf(1)
        statistic.authorName = userName
        statistic.postedDate = LocalDate.now()
        return statistic
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

    fun buildDayStatisticForUserInChat(chatId: Long, userId: Long): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Your message statistics for this day:").append("\n")
        val statistic = getUserStatisticForCurrentDayInChat(userId, chatId)
        if (statistic != null) {
            stringBuilder.append(statistic.authorName)
                    .append(" ")
                    .append(statistic.messageCount)
            return stringBuilder.toString()
        } else return "Your statistic is empty for today"
    }


}