package com.github.bumblebee.command.start

import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Component
import telegram.api.BotApi
import telegram.domain.Update
import telegram.domain.request.ParseMode
import telegram.polling.UpdateHandler
import java.io.IOException
import java.io.StringWriter

@Component
class StartCommand(val botApi: BotApi) : UpdateHandler {
    private val helpText: String by lazy {
        try {
            StartCommand::class.java.classLoader.getResourceAsStream("start.md").use { stream ->
                val sw = StringWriter()
                IOUtils.copy(stream, sw)
                return@lazy sw.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun onUpdate(update: Update): Boolean {
        botApi.sendMessage(update.message.chat.id, helpText, ParseMode.MARKDOWN, null,
                update.message.messageId, null)
        return true
    }

}