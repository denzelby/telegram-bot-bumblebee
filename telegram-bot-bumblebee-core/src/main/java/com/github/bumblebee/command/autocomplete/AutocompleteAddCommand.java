package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.security.BumblebeeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

/**
 * Created by Fare on 14.06.2016.
 */

@Component
public class AutocompleteAddCommand extends SingleArgumentCommand{

    private final BotApi botApi;
    private AutocompleteHandler handler;
    private BumblebeeSecurityConfig securityConfig;

    @Autowired
    public AutocompleteAddCommand(BotApi botApi,AutocompleteHandler handler, BumblebeeSecurityConfig securityConfig){
        this.botApi = botApi;
        this.handler = handler;
        this.securityConfig = securityConfig;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument){

        //todo: implement proper security model
        if(! securityConfig.getAdminIds().contains(update.getMessage().getFrom().getId().toString())){
            botApi.sendMessage(chatId,"You are not allowed to execute this command");
            return;
        }
        if(argument == null || !argument.contains("/") || argument.startsWith(" ")){
            botApi.sendMessage(chatId,"wrong template, try again");
            return;
        }

        String patternKey = argument.substring(0, argument.indexOf('/'));

        if(patternKey.length()+1>=argument.length()){
            botApi.sendMessage(chatId,"wrong template, try again");
            return;
        }

        String[] patternValue = argument.replaceFirst(patternKey+"/","").split("/");
        if(handler.addAutocompleteCommand(patternKey,patternValue)){
            botApi.sendMessage(chatId,"pattern "+patternKey+" successfully added");
        }

    }
}
