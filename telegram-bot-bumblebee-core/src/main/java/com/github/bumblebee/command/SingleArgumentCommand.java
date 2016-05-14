package com.github.bumblebee.command;

import telegram.domain.Update;
import telegram.polling.UpdateHandler;

public abstract class SingleArgumentCommand implements UpdateHandler {

    public abstract void handleCommand(Update update, Long chatId, String argument);

    @Override
    public boolean onUpdate(Update update) {
        Long chatId = update.getMessage().getChat().getId();
        String text = update.getMessage().getText();
        int cmdEndIndex = text.indexOf(' ');

        String argument = null;
        if (cmdEndIndex > 0 && cmdEndIndex < text.length() - 1) {
            argument = text.substring(cmdEndIndex+1);
        }

        handleCommand(update, chatId, argument);
        return true;
    }
}
