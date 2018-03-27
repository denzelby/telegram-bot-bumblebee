package com.github.bumblebee.command.statistics.service

import com.github.bumblebee.command.statistics.dao.StatisticsRepository
import com.github.bumblebee.command.statistics.entity.Statistic
import com.github.bumblebee.util.logger
import com.github.telegram.domain.Message
import com.github.telegram.domain.User
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class StatisticsService(private val repository: StatisticsRepository) {

    private val stats: MutableMap<Long, MutableList<Statistic>> = loadPersistentStat()

    private fun loadPersistentStat(): MutableMap<Long, MutableList<Statistic>> {
        return repository.findStatisticByPostedDate(LocalDate.now())
                .groupByTo(ConcurrentHashMap()) { it.chatId }
    }

    fun getStatistics(): Map<Long, List<Statistic>> = stats

    fun handleMessage(message: Message, from: User) {
        stats.compute(message.chat.id, { _, existingChatStats ->
            val chatStats = existingChatStats ?: mutableListOf()

            // get or create Statistics object
            val userStat = Optional.ofNullable(chatStats.find { it.authorId == from.id }).orElseGet {
                val stat = createInitialStat(message)
                chatStats.add(stat)
                stat
            }

            userStat.messageCount++
            userStat.authorName = formatUserName(from)
            repository.save(userStat)

            log.debug("Saved stat: {}", userStat)
            chatStats
        })
    }

    fun cleanupStats(date: LocalDate) {
        log.debug("Stat before cleanup: {}", stats)
        stats.values.forEach { chatStats ->
            chatStats.removeIf { date != it.postedDate }
        }
        log.debug("Stat after cleanup: {}", stats)
    }

    fun getAllStatInChatByUsers(chatId: Long): Map<String?, Int> = repository.findStatisticByChatId(chatId)
            .groupBy { it.authorId }
            .mapKeys { it.value.sortedByDescending { it.postedDate }.first().authorName }
            .mapValues { it.value.sumBy { it.messageCount } }

    private fun createInitialStat(message: Message): Statistic = with(Statistic()) {
        postedDate = message.postedDate()
        chatId = message.chat.id
        authorId = message.from!!.id
        this
    }

    private fun Message.postedDate(): LocalDate {
        return Instant.ofEpochSecond(date.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun formatUserName(from: User): String = when {
        !from.lastName.isNullOrBlank() -> "${from.firstName} ${from.lastName}"
        !from.firstName.isBlank() -> from.firstName
        !from.userName.isNullOrBlank() -> from.userName!!
        else -> from.id.toString()
    }

    companion object {
        val log = logger<StatisticsService>()
    }
}
