package com.github.bumblebee.command.statistics

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.statistics.service.StatisticsService
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class MessageStatisticsViewCommand(private val botApi: BotApi,
                                   private val statisticsService: StatisticsService,
                                   private val randomPhraseService: RandomPhraseService) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        when (argument) {
            null -> botApi.sendMessage(chatId, statisticsService.buildStatisticsForCurrentDayInChat(chatId))
            "me" -> botApi.sendMessage(chatId, statisticsService.buildDayStatisticForUserInChat(chatId, update.senderId))
            else -> botApi.sendMessage(chatId, randomPhraseService.surprise())
        }

    }

}