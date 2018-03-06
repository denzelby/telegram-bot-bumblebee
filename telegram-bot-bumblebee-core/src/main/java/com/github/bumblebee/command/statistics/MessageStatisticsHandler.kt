package com.github.bumblebee.command.statistics

import com.github.bumblebee.command.ChainedMessageListener
import com.github.bumblebee.command.statistics.service.MessageStatHandler
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class MessageStatisticsHandler(private val botApi: BotApi,
                               private val messageHandler: MessageStatHandler) : ChainedMessageListener() {

    override fun onMessage(chatId: Long, message: String?, update: Update): Boolean {
        val msg = update.message ?: return false
        val from = msg.from
        if (from == null || from.isBot) {
            return false
        }

        messageHandler.handleMessage(msg, from)
        return false
    }

//    @Scheduled(cron = "0 59 23 * * *")
//    fun postStatistics() {
//        statisticsService.getChatsWithStatistic().forEach { chatId ->
//            botApi.sendMessage(chatId, statisticsService.buildStatisticsForCurrentDayInChat(chatId))
//        }
//    }
}