package com.github.bumblebee.command.autocomplete

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class AutoCompletePhraseView(
    private val botApi: BotApi,
    private val handler: AutoCompleteHandler
) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        botApi.sendMessage(chatId, handler.availableCompletions().toString())
    }
}
