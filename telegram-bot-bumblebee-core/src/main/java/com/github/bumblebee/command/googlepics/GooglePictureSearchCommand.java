package com.github.bumblebee.command.googlepics;

import com.github.bumblebee.command.SingleArgumentCommand;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import telegram.api.BotApi;
import telegram.domain.Update;

public class GooglePictureSearchCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final GooglePicsApi googlePicsApi;

    public GooglePictureSearchCommand(BotApi botApi) {

        this.botApi = botApi;
        googlePicsApi = Feign.builder()
                .decoder(new GsonDecoder())
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .target(GooglePicsApi.class, "https://ajax.googleapis.com/ajax/services");
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, "Wut?");
            return;
        }

        GooglePicsResponse response = googlePicsApi.queryPictures(argument);

        botApi.sendMessage(chatId, response.getResponseData().getResults().get(0).getUrl());
    }
}
