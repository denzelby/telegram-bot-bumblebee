package com.github.bumblebee.command.status;

import com.github.bumblebee.command.SingleArgumentCommand;
import telegram.api.BotApi;
import telegram.domain.Update;

public class StatusCommand extends SingleArgumentCommand {

    private final BotApi botApi;

    public StatusCommand(BotApi botApi) {

        this.botApi = botApi;
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {
        botApi.sendMessage(chatId, "I'm fine!");
    }
}
