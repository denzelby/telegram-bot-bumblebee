package com.github.bumblebee.command

import com.github.bumblebee.bot.consumer.UpdateHandler
import com.github.telegram.domain.Update

abstract class ChainedMessageListener : UpdateHandler {

    abstract fun onMessage(chatId: Long, message: String?, update: Update): Boolean

    override fun onUpdate(update: Update): Boolean {
        // 'edits' and other types of updates aren't supported for now
        val message = update.message
        if (message != null) {
            val chatId = message.chat.id
            val text = message.text

            return onMessage(chatId, text, update)
        }
        return false
    }
}
