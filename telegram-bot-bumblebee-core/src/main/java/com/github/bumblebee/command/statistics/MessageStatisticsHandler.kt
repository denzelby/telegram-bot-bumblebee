package com.github.bumblebee.command.statistics

import com.github.bumblebee.command.ChainedMessageListener
import com.github.bumblebee.command.statistics.service.StatisticsService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MessageStatisticsHandler(private val botApi: BotApi,
                               private val statisticsService: StatisticsService) : ChainedMessageListener() {

    override fun onMessage(chatId: Long, message: String?, update: Update): Boolean {
        val userId = update.message?.from?.id ?: return false
        statisticsService.saveOrUpdateUserStatistic(userId, chatId, update.message?.from?.userName)
        return false
    }

    @Scheduled(cron = "0 59 23 * * *")
    fun postStatistics() {
        statisticsService.getChatsWithStatistic().forEach { chatId ->
            botApi.sendMessage(chatId, statisticsService.buildStatisticsForCurrentDayInChat(chatId))
        }
    }
}