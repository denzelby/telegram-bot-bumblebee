package com.github.bumblebee.command.autocomplete

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.security.PrivilegedCommand
import com.github.bumblebee.security.UnauthorizedRequestAware
import com.github.bumblebee.security.UserRole
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update


@PrivilegedCommand(name = "AutoCompleteAdminCommand", role = UserRole.MODERATOR)
class AutoCompleteAdminCommand(
    private val botApi: BotApi, private val handler: AutoCompleteHandler,
    private val randomPhraseService: RandomPhraseService
) : SingleArgumentCommand(), UnauthorizedRequestAware {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        if (argument == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise())
            return
        }

        val added = handler.addCompletion(argument.trim())
        if (added)
            botApi.sendMessage(chatId, "Pattern successfully added.")
        else
            botApi.sendMessage(chatId, "Wrong template, try again.")
    }

    override fun onUnauthorizedRequest(update: Update) {
        botApi.sendMessage(update.senderId,
            "You are not allowed to execute this command. Become a moderator to do this.")
    }
}
