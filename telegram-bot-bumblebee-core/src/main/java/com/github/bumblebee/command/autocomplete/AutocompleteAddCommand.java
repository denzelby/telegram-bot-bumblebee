package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.security.BumblebeeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;


@Component
public class AutocompleteAddCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final AutocompleteHandler handler;
    private final BumblebeeSecurityConfig securityConfig;

    @Autowired
    public AutocompleteAddCommand(BotApi botApi, AutocompleteHandler handler, BumblebeeSecurityConfig securityConfig) {
        this.botApi = botApi;
        this.handler = handler;
        this.securityConfig = securityConfig;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {

        //todo: implement proper security model
        if (!securityConfig.getAdminIds().contains(update.getMessage().getFrom().getId())) {
            botApi.sendMessage(chatId, "You are not allowed to execute this command");
            return;
        }

        if(argument==null || !handler.addTemplates(argument.trim())){
            botApi.sendMessage(chatId, "wrong template, try again");
            return;
        }

        botApi.sendMessage(chatId, "pattern successfully added");
    }

}
