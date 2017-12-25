package com.github.bumblebee.command.start

import com.github.telegram.api.BotApi
import com.github.telegram.domain.ParseMode
import com.github.telegram.domain.Update
import org.apache.commons.io.FileUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import telegram.polling.UpdateHandler

@Component
class StartCommand(val botApi: BotApi) : UpdateHandler {
    private val helpText: String by lazy {
        FileUtils.readFileToString(ClassPathResource("start.md").file)
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