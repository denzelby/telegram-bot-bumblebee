package com.github.bumblebee.command

import com.github.bumblebee.bot.consumer.UpdateHandler
import com.github.telegram.domain.Update

abstract class SingleArgumentCommand : UpdateHandler {

    abstract fun handleCommand(update: Update, chatId: Long, argument: String?)

    override fun onUpdate(update: Update): Boolean {
        val chatId = update.senderId
        val text = update.message?.text ?: ""
        val cmdEndIndex = text.indexOf(' ')

        val argument = if (cmdEndIndex > 0 && cmdEndIndex < text.length - 1) {
            text.substring(cmdEndIndex + 1)
        } else null

        handleCommand(update, chatId, argument)
        return true
    }
}