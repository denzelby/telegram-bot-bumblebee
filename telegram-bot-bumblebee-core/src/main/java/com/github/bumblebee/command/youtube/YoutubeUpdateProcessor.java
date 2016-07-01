package com.github.bumblebee.command.youtube;

import com.github.bumblebee.command.youtube.service.ChatIdForYoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class YoutubeUpdateProcessor {

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private final YoutubeVideoSender sender;
    private final ChatIdForYoutubeService service;
    private final YoutubeChatCommand command;

    @Autowired
    public YoutubeUpdateProcessor(YoutubeVideoSender sender, ChatIdForYoutubeService service, YoutubeChatCommand command) {

        this.sender = sender;
        this.service = service;
        this.command = command;

    }

    public void process(String videoId) {

        for (Long chatId : command.getChatIds())
            sender.sendVideo(chatId, VIDEO_URL + videoId);
    }

}
