package com.github.bumblebee.command.statistics

import com.github.bumblebee.command.ChainedMessageListener
import com.github.bumblebee.command.statistics.service.StatisticsService
import com.github.bumblebee.util.logger
import com.github.telegram.domain.ChatType
import com.github.telegram.domain.Update
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class MessageStatisticsHandler(private val statistics: StatisticsService) : ChainedMessageListener() {

    override fun onMessage(chatId: Long, message: String?, update: Update): Boolean {
        val msg = update.message ?: return false
        val from = msg.from
        if (from == null || from.isBot || msg.chat.type == ChatType.private) {
            return false
        }

        statistics.handleMessage(msg, from)
        return false
    }

    @Scheduled(cron = "0 00 00 * * *")
    fun cleanup() {
        logger<MessageStatisticsHandler>().info("Statistics cleanup")
        statistics.cleanupStats(LocalDate.now())
    }
}
