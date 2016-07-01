package com.github.bumblebee.command.youtube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;


@Component
public class YoutubeVideoSender {

    private final BotApi botapi;

    @Autowired
    public YoutubeVideoSender(BotApi botapi) {
        this.botapi = botapi;
    }

    public void sendVideo(Long chatId, String argument) {

        botapi.sendMessage(chatId, argument);
    }
}
