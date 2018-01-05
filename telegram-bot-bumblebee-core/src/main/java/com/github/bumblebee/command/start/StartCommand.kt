package com.github.bumblebee.command.start

import com.github.bumblebee.bot.consumer.UpdateHandler
import com.github.telegram.api.BotApi
import com.github.telegram.domain.ParseMode
import com.github.telegram.domain.Update
import org.apache.commons.io.IOUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class StartCommand(val botApi: BotApi) : UpdateHandler {
    private val helpText: String by lazy {
        IOUtils.toString(ClassPathResource("start.md").inputStream)
    }

    override fun onUpdate(update: Update): Boolean {

        botApi.sendMessage(
                chatId = update.message!!.chat.id,
                text = helpText,
                parseMode = ParseMode.MARKDOWN,
                replyToMessageId = update.message!!.messageId
        )

        return true
    }

}