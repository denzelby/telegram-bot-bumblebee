package com.github.bumblebee.command

import com.github.telegram.domain.Update
import telegram.polling.UpdateHandler

abstract class ChainedMessageListener : UpdateHandler {

    abstract fun onMessage(chatId: Long, message: String?, update: Update): Boolean

    override fun onUpdate(update: Update): Boolean {
        val chatId = update.message!!.chat.id
        val text = update.message!!.text

        return onMessage(chatId, text, update)
    }
}
