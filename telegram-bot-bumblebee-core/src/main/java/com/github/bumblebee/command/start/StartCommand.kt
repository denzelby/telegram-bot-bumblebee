package com.github.bumblebee.command.start

import org.apache.commons.io.FileUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import telegram.api.BotApi
import telegram.domain.Update
import telegram.domain.request.ParseMode
import telegram.polling.UpdateHandler

@Component
class StartCommand(val botApi: BotApi) : UpdateHandler {
    private val helpText: String by lazy {
        FileUtils.readFileToString(ClassPathResource("start.md").file)
    }

    override fun onUpdate(update: Update): Boolean {
        botApi.sendMessage(update.message.chat.id, helpText, ParseMode.MARKDOWN, null,
                update.message.messageId, null)
        return true
    }

}