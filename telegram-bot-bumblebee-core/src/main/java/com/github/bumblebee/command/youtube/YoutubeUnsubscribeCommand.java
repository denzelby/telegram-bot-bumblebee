package com.github.bumblebee.command.youtube;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.youtube.entity.Chat;
import com.github.bumblebee.command.youtube.entity.Subscription;
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService;
import com.github.bumblebee.service.RandomPhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.util.Iterator;
import java.util.List;

@Component
public class YoutubeUnsubscribeCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final YoutubeSubscriptionService service;
    private final RandomPhraseService randomPhraseService;

    @Autowired
    public YoutubeUnsubscribeCommand(BotApi botApi,
                                     YoutubeSubscriptionService service,
                                     RandomPhraseService randomPhraseService) {
        this.botApi = botApi;
        this.service = service;
        this.randomPhraseService = randomPhraseService;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String channelId) {

        if (channelId == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise());
            return;
        }

        for (Subscription sub : service.getExistingSubscriptions()) {
            if (sub.getChannelId().equals(channelId)) {
                processUnsubscription(sub, channelId, chatId);
                return;
            }
        }
        botApi.sendMessage(chatId, "Channel to unsubscribe not exist!");
    }

    private void processUnsubscription(Subscription sub, String channelId, Long chatId) {
        List<Chat> chats = sub.getChats();
        for (Chat chat : chats) {
            if (chat.getChatId().equals(chatId)) {
                if (chats.size() == 1) {
                    removeChannel(sub, channelId, chatId);
                    return;
                }
                if (chats.size() > 1) {
                    chats.remove(chat);
                    service.storeSubscription(sub);
                    botApi.sendMessage(chatId, "Chat successfully unsubscribed!");
                    return;
                }
            }
        }
    }

    private void removeChannel(Subscription sub, String channelId, Long chatId) {
        if (service.unsubscribeChannel(channelId)) {
            service.deleteSubscription(sub);
            service.getExistingSubscriptions().remove(sub);
            botApi.sendMessage(chatId, "Channel removed!");
        }
    }

}
