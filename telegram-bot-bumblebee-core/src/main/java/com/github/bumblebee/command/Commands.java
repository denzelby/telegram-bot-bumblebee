package com.github.bumblebee.command;

import com.github.bumblebee.command.googlepics.GooglePictureSearchCommand;
import com.github.bumblebee.command.status.StatusCommand;
import telegram.api.BotApi;
import telegram.api.FileApi;
import telegram.polling.UpdateHandler;

public final class Commands {

    private final BotApi botApi;
    private final FileApi fileApi;

    public Commands(BotApi botApi, FileApi fileApi) {

        this.botApi = botApi;
        this.fileApi = fileApi;
    }

    public UpdateHandler statusCommand() {
        return new StatusCommand(botApi);
    }

    public UpdateHandler googlePictureSearchCommand() {
        return new GooglePictureSearchCommand(botApi);
    }
}
