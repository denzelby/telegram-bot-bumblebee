package com.github.bumblebee.command

import telegram.domain.Update
import telegram.polling.UpdateHandler

abstract class SingleArgumentCommand : UpdateHandler {

    abstract fun handleCommand(update: Update, chatId: Long, argument: String?)

    override fun onUpdate(update: Update): Boolean {
        val chatId = update.message.chat.id
        val text = update.message.text
        val cmdEndIndex = text.indexOf(' ')

        val argument = if (cmdEndIndex > 0 && cmdEndIndex < text.length - 1) {
            text.substring(cmdEndIndex + 1)
        } else null

        handleCommand(update, chatId, argument)
        return true
    }
}