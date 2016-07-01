package com.github.bumblebee.command.youtube;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.youtube.entity.ChatId;
import com.github.bumblebee.command.youtube.service.ChatIdForYoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class YoutubeChatCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final ChatIdForYoutubeService service;
    private final Set<Long> chatIds;

    @Autowired
    public YoutubeChatCommand(BotApi botApi, ChatIdForYoutubeService service) {
        this.botApi = botApi;
        this.service = service;
        chatIds = service.retrieveChatIds().stream()
                .map(ChatId::getChatId).collect(Collectors.toSet());
    }

    public Set<Long> getChatIds() {
        return chatIds;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {
        if (!chatIds.contains(chatId)) {
            chatIds.add(chatId);
            service.storeChatId(new ChatId(chatId));
        }
    }
}
