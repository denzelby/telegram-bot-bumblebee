package com.github.bumblebee;

import com.github.bumblebee.command.Commands;
import telegram.api.BotApi;
import telegram.api.FileApi;
import telegram.polling.TelegramUpdateService;

import java.util.Properties;

public class BumblebeeBotApp {

    public static void main(String[] args) {

        BumblebeeBot bee = new BumblebeeBot();
        BotApi botApi = bee.create();
        FileApi fileApi = bee.createFileApi();
        TelegramUpdateService updateService = new TelegramUpdateService(botApi);

        registerHandlers(updateService, botApi, fileApi, bee.getConfig());

        updateService.startPolling();
    }

    private static void registerHandlers(TelegramUpdateService updateService, BotApi botApi, FileApi fileApi,
                                         Properties config) {

        Commands commands = new Commands(botApi, fileApi, config);

        updateService
                .bind(commands.googlePictureSearchCommand(), "/g", "/pic", "/p")
                .bind(commands.youtubeSearchCommand(), "/y", "/youtube", "/v", "/video")
                .bind(commands.statusCommand(), "/status")
                .bind(commands.startCommand(), "/start");
    }
}
