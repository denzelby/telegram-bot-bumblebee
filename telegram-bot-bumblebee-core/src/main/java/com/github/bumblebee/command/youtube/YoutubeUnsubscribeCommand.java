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

        for (Subscription sub : service.getExistingSubscriptions())
            if (sub.getChannelId().equals(channelId)) {
                for (Chat chat : sub.getChats()) {
                    if (chat.getChatId().equals(chatId)) {
                        sub.getChats().remove(chat);
                        service.storeSubscription(sub);
                        botApi.sendMessage(chatId, "Successfully unsubscribe this chat");
                        return;
                    }
                }
                if (service.unsubscribeChannel(channelId)) {
                    service.deleteSubscription(sub);
                    service.getExistingSubscriptions().remove(sub);
                    botApi.sendMessage(chatId, "Channel removed");
                    return;
                }
            }
        botApi.sendMessage(chatId, "Channel to unsubscribe not already exist!");
    }
}
