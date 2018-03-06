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
class MessageStatHandler(private val repository: StatisticsRepository) {

    private val stats: MutableMap<Long, MutableList<Statistic>> = loadPersistentStat()

    private fun loadPersistentStat(): MutableMap<Long, MutableList<Statistic>> {
        return repository.findStatisticByPostedDate(LocalDate.now())
                .groupByTo(ConcurrentHashMap()) { it.chatId }
    }

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

    private fun createInitialStat(message: Message): Statistic = with(Statistic()) {
        postedDate = Instant.ofEpochSecond(message.date.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
        chatId = message.chat.id
        authorId = message.from!!.id
        this
    }

    private fun formatUserName(from: User): String {
        val fullName = "${from.firstName} ${from.lastName}"
        return if (fullName.isNotBlank()) fullName
        else (from.userName ?: from.id.toString())
    }

    companion object {
        val log = logger<MessageStatHandler>()
    }
}
