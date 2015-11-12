package com.github.bumblebee;

import com.github.bumblebee.command.Commands;
import telegram.api.BotApi;
import telegram.api.FileApi;
import telegram.polling.TelegramUpdateService;

public class BumblebeeBotApp {

    public static void main(String[] args) {

        BumblebeeBot bee = new BumblebeeBot();
        BotApi botApi = bee.create();
        FileApi fileApi = bee.createFileApi();
        TelegramUpdateService updateService = new TelegramUpdateService(botApi);

        registerHandlers(updateService, botApi, fileApi);

        updateService.startPolling();
    }

    private static void registerHandlers(TelegramUpdateService updateService, BotApi botApi, FileApi fileApi) {

        Commands commands = new Commands(botApi, fileApi);

        updateService
                .bind(commands.googlePictureSearchCommand(), "/g", "/pic", "/p")
                .bind(commands.statusCommand(), "/status");
    }
}
