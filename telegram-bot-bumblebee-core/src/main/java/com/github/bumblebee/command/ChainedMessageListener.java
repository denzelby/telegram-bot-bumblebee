package com.github.bumblebee.command;

import telegram.domain.Update;
import telegram.polling.UpdateHandler;

public abstract class ChainedMessageListener implements UpdateHandler {

    public abstract boolean onMessage(Long chatId, String message, Update update);

    @Override
    public boolean onUpdate(Update update) {
        Long chatId = update.getMessage().getChat().getId();
        String text = update.getMessage().getText();

        return onMessage(chatId, text, update);
    }
}
