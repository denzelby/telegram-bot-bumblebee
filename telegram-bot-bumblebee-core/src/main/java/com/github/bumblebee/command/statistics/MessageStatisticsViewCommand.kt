package com.github.bumblebee.command.statistics

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.statistics.service.StatisticsService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class MessageStatisticsViewCommand(private val botApi: BotApi,
                                   private val service: StatisticsService) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.


    }

}