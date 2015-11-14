package com.github.bumblebee.command;

import com.github.bumblebee.command.googlepics.GooglePictureSearchCommand;
import com.github.bumblebee.command.start.StartCommand;
import com.github.bumblebee.command.status.StatusCommand;
import com.github.bumblebee.command.youtube.YoutubeSearchCommand;
import com.github.bumblebee.service.RandomPhraseService;
import telegram.api.BotApi;
import telegram.api.FileApi;
import telegram.polling.UpdateHandler;

import java.util.Properties;

public final class Commands {

    private RandomPhraseService phraseService = new RandomPhraseService();
    private final BotApi botApi;
    private final FileApi fileApi;
    private final Properties config;

    public Commands(BotApi botApi, FileApi fileApi, Properties config) {

        this.botApi = botApi;
        this.fileApi = fileApi;
        this.config = config;
    }

    public UpdateHandler statusCommand() {
        return new StatusCommand(botApi);
    }

    public UpdateHandler startCommand() {
        return new StartCommand(botApi);
    }

    public UpdateHandler googlePictureSearchCommand() {
        return new GooglePictureSearchCommand(botApi);
    }

    public UpdateHandler youtubeSearchCommand() {
        return new YoutubeSearchCommand(botApi, phraseService,
                config.getProperty("youtube.api.projectname"),
                config.getProperty("youtube.api.key"));
    }
}
