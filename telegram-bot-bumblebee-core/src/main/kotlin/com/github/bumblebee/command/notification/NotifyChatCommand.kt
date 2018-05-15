package com.github.bumblebee.command.notification

import com.github.bumblebee.bot.consumer.UpdateHandler
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.ChatType
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class NotifyChatCommand(private val botApi: BotApi,
                        private val randomPhraseService: RandomPhraseService) : UpdateHandler {

    override fun onUpdate(update: Update): Boolean {
        if (update.message?.chat?.type == ChatType.private) {
            botApi.sendMessage(update.senderId, randomPhraseService.surprise())
            return true
        }

        val response = botApi.getChatAdministrators(update.senderId)

        if (response.ok) {
            val members = response.result.orEmpty()
            val mentions = members.asSequence()
                    .filter { !it.user.isBot }
                    .map { it.user.userName }
                    .filterNotNull()
                    .joinToString(separator = " ") { "@$it" }

            val message = if (mentions.isNotEmpty()) mentions else randomPhraseService.no()
            botApi.sendMessage(update.senderId, message)
        }

        return true
    }
}
