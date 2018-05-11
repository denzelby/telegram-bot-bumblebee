package com.github.bumblebee.command

import com.github.bumblebee.bot.consumer.UpdateHandler
import com.github.telegram.domain.Update

abstract class ChainedMessageListener : UpdateHandler {
    abstract fun onMessage(chatId: Long, message: String?, update: Update): Boolean

    override fun onUpdate(update: Update): Boolean =
        update.message?.let { onMessage(it.chat.id, it.text, update) } ?: false
}
