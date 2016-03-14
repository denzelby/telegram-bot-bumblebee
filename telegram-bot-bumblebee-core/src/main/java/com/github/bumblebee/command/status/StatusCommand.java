package com.github.bumblebee.command.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.bumblebee.command.SingleArgumentCommand;

import telegram.api.BotApi;
import telegram.domain.Update;

@Component
public class StatusCommand extends SingleArgumentCommand {

    private final BotApi botApi;

    @Autowired
    public StatusCommand(BotApi botApi) {

        this.botApi = botApi;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {
        botApi.sendMessage(chatId, "I'm fine!");
    }

}
