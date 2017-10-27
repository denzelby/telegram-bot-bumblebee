package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.SingleArgumentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

@Component
public class AutoCompletePhraseView extends SingleArgumentCommand {

    private final BotApi botApi;
    private final AutoCompleteHandler handler;

    @Autowired
    public AutoCompletePhraseView(BotApi botApi, AutoCompleteHandler handler) {
        this.botApi = botApi;
        this.handler = handler;
    }

    @Override
    public void handleCommand(Update update, long chatId, String argument) {
        botApi.sendMessage(chatId, handler.getAutoCompletes().toString());
    }
}
