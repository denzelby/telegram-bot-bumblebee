package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.SingleArgumentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

/**
 * Created by Misha on 14.05.2016.
 */
@Component
public class AutocompleteHandler extends SingleArgumentCommand {

    private final BotApi botApi;

    @Autowired
    public AutocompleteHandler(BotApi botApi){
        this.botApi = botApi;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {
        if(!update.getMessage().getText().isEmpty()){
            if (update.getMessage().getText().equals(QuestionTemplate.DO_SVI_DOS )){
                botApi.sendMessage(chatId, "СВИ");
                botApi.sendMessage(chatId, "ДОС");
            }
            if (update.getMessage().getText().equals(QuestionTemplate.DOS_VI_DOS)){
                botApi.sendMessage(chatId, "ВИ");
                botApi.sendMessage(chatId, "ДОС");
            }
        }
    }
}
