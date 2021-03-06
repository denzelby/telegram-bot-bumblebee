package com.github.bumblebee.command.youtube

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.youtube.entity.Subscription
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class YoutubeUnsubscribeCommand(private val botApi: BotApi,
                                private val service: YoutubeSubscriptionService,
                                private val randomPhraseService: RandomPhraseService) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        if (argument == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise())
            return
        }

        val channelId: String = argument
        service.getSubscriptions().forEach { sub ->
            if (sub.channelId == channelId) {
                processUnsubscription(sub, channelId, chatId)
                return
            }
        }
        botApi.sendMessage(chatId, "Channel to unsubscribe not exist!")
    }

    private fun processUnsubscription(sub: Subscription, channelId: String, chatId: Long) {
        val chats = sub.chats
        for (chat in chats) {
            if (chat.chatId == chatId) {
                if (chats.size == 1) {
                    removeChannel(sub, channelId, chatId)
                    return
                }
                if (chats.size > 1) {
                    chats.remove(chat)
                    service.storeSubscription(sub)
                    botApi.sendMessage(chatId, "Chat successfully unsubscribed!")
                    return
                }
            }
        }
    }

    private fun removeChannel(sub: Subscription, channelId: String, chatId: Long) {
        if (service.unsubscribeChannel(channelId)) {
            service.deleteSubscription(sub)
            service.getSubscriptions().remove(sub)
            botApi.sendMessage(chatId, "Channel removed!")
        }
    }
}
