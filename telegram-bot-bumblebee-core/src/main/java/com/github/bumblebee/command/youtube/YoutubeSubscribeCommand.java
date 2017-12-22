package com.github.bumblebee.command.youtube;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.youtube.entity.Chat;
import com.github.bumblebee.command.youtube.entity.Subscription;
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService;
import com.github.bumblebee.service.RandomPhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.telegram.api.BotApi;
import com.github.telegram.domain.Update;

import java.util.*;


@Component
public class YoutubeSubscribeCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final YoutubeSubscriptionService service;
    private final RandomPhraseService randomPhraseService;
    private final List<Subscription> subscriptionList;

    @Autowired
    public YoutubeSubscribeCommand(BotApi botApi,
                                   YoutubeSubscriptionService service,
                                   RandomPhraseService randomPhraseService) {
        this.botApi = botApi;
        this.service = service;
        this.randomPhraseService = randomPhraseService;
        this.subscriptionList = service.getExistingSubscriptions();
    }

    @Override
    public void handleCommand(Update update, long chatId, String channelId) {

        if (channelId == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise());
            return;
        }

        for (Subscription sub : subscriptionList) {
            if (sub.getChannelId().equals(channelId)) {
                if (checkForExistingChatInSubscription(sub, chatId)) {
                    botApi.sendMessage(chatId, "Subscription already available for this chat!");
                    return;
                } else {
                    addNewChatToSubscription(sub, chatId);
                    botApi.sendMessage(chatId, "Subscription successfully added for this chat!");
                    return;
                }
            }
        }

        if (service.subscribeChannel(channelId)) {
            createAndStoreNewSubscription(channelId, chatId);
            botApi.sendMessage(chatId, "New channel successfully added!");
        } else {
            botApi.sendMessage(chatId, "Wrong channel, cannot subscribe!");
        }

    }

    private boolean checkForExistingChatInSubscription(Subscription sub, Long chatId) {
        return sub.getChats()
                .stream()
                .anyMatch(chat -> chat.getChatId().equals(chatId));
    }

    private void addNewChatToSubscription(Subscription sub, Long chatId) {
        sub.getChats().add(new Chat(chatId, sub));
        service.storeSubscription(sub);
    }

    private void createAndStoreNewSubscription(String argument, Long chatId) {
        Subscription subscription = new Subscription();
        subscription.setChannelId(argument);
        subscription.setUpdatedDate(new Date());
        subscription.getChats().add(new Chat(chatId, subscription));
        subscriptionList.add(subscription);
        service.storeSubscription(subscription);
    }
}
