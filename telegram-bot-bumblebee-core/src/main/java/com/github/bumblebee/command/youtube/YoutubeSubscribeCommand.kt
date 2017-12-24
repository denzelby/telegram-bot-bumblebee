package com.github.bumblebee.command.youtube

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.youtube.entity.Chat
import com.github.bumblebee.command.youtube.entity.Subscription
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import java.util.*


@Component
class YoutubeSubscribeCommand(private val botApi: BotApi,
                              private val service: YoutubeSubscriptionService,
                              private val randomPhraseService: RandomPhraseService) : SingleArgumentCommand() {

    private val subscriptionList: MutableList<Subscription> = service.existingSubscriptions.toMutableList()

    override fun handleCommand(update: Update, chatId: Long, channelId: String?) {

        if (channelId == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise()).execute()
            return
        }

        subscriptionList
                .filter { it.channelId == channelId }
                .forEach {
                    if (checkForExistingChatInSubscription(it, chatId)) {
                        botApi.sendMessage(chatId, "Subscription already available for this chat!").execute()
                        return
                    } else {
                        addNewChatToSubscription(it, chatId)
                        botApi.sendMessage(chatId, "Subscription successfully added for this chat!").execute()
                        return
                    }
                }

        if (service.subscribeChannel(channelId)) {
            createAndStoreNewSubscription(channelId, chatId)
            botApi.sendMessage(chatId, "New channel successfully added!").execute()
        } else {
            botApi.sendMessage(chatId, "Wrong channel, cannot subscribe!").execute()
        }

    }

    private fun checkForExistingChatInSubscription(sub: Subscription, chatId: Long?): Boolean {
        return sub.chats.any { it.chatId == chatId }
    }

    private fun addNewChatToSubscription(sub: Subscription, chatId: Long?) {
        sub.chats.add(Chat(chatId, sub))
        service.storeSubscription(sub)
    }

    private fun createAndStoreNewSubscription(argument: String, chatId: Long?) {
        val subscription = Subscription()
        subscription.channelId = argument
        subscription.updatedDate = Date()
        subscription.chats.add(Chat(chatId, subscription))
        subscriptionList.add(subscription)
        service.storeSubscription(subscription)
    }
}
