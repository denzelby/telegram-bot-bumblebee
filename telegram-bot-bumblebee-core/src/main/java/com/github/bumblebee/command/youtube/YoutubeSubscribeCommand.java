package com.github.bumblebee.command.youtube;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.youtube.api.YoutubeSubscriptionProvider;
import com.github.bumblebee.command.youtube.entity.Chat;
import com.github.bumblebee.command.youtube.entity.Subscription;
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService;
import com.github.bumblebee.service.RandomPhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.util.*;


@Component
public class YoutubeSubscribeCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final YoutubeSubscriptionService service;
    private final RandomPhraseService randomPhraseService;
    private final YoutubeSubscriptionProvider provider;
    private final List<Subscription> subscriptionList;

    @Autowired
    public YoutubeSubscribeCommand(BotApi botApi,
                                   YoutubeSubscriptionService service,
                                   RandomPhraseService randomPhraseService,
                                   YoutubeSubscriptionProvider provider) {
        this.botApi = botApi;
        this.service = service;
        this.randomPhraseService = randomPhraseService;
        this.provider = provider;
        this.subscriptionList = service.retrieveSubscriptions();
    }

    public Set<Long> getChatIds(String channelId) {
        Set<Long> idSet = new HashSet<>();
        for (Subscription sub : subscriptionList) {
            if (sub.getChannelId().equals(channelId)) {
                for (Chat chat : sub.getChats()) {
                    idSet.add(chat.getChatId());
                }
            }
        }
        return idSet;
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise());
            return;
        }

        for (Subscription sub : subscriptionList) {
            if (sub.getChannelId().equals(argument)) {
                if (checkForExistingChatInSubscription(sub, chatId)) {
                    botApi.sendMessage(chatId, "Subscription already available fo this chat!");
                    return;
                } else {
                    addNewChatToSubscription(sub, chatId);
                    botApi.sendMessage(chatId, "Successfully add this chat to subscription");
                    return;
                }
            }
        }

        if (provider.subscribeChannel(argument)) {
            createAndStoreNewSubscription(argument, chatId);
            botApi.sendMessage(chatId, "New channel successfully added");
        } else {
            botApi.sendMessage(chatId, "Wrong channel, cannot subscribe!");
        }

    }

    private boolean checkForExistingChatInSubscription(Subscription sub, Long chatId) {
        for (Chat chat : sub.getChats()) {
            if (chat.getChatId().equals(chatId)) {
                return true;
            }
        }
        return false;
    }

    private void addNewChatToSubscription(Subscription sub, Long chatId) {
        sub.getChats().add(new Chat(chatId, sub));
        service.storeSubscription(sub);
    }

    private void createAndStoreNewSubscription(String argument, Long chatId) {
        Date date = new Date();
        Subscription subscription = new Subscription();
        subscription.setChannelId(argument);
        subscription.setUpdatedDate(date);
        subscription.getChats().add(new Chat(chatId, subscription));
        subscriptionList.add(subscription);
        service.storeSubscription(subscription);
    }
}
