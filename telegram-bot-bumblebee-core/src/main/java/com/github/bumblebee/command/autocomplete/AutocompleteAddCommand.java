package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.security.BumblebeeSecurityConfig;
import com.github.bumblebee.service.RandomPhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;


@Component
public class AutocompleteAddCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final AutocompleteHandler handler;
    private final BumblebeeSecurityConfig securityConfig;
    protected final RandomPhraseService randomPhrase;

    @Autowired
    public AutocompleteAddCommand(BotApi botApi, AutocompleteHandler handler, BumblebeeSecurityConfig securityConfig, RandomPhraseService randomPhrase) {
        this.botApi = botApi;
        this.handler = handler;
        this.securityConfig = securityConfig;
        this.randomPhrase = randomPhrase;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhrase.surprise());
            return;
        }

        //todo: implement proper security model
        if (!securityConfig.getAdminIds().contains(update.getMessage().getFrom().getId())) {
            botApi.sendMessage(chatId, "You are not allowed to execute this command.");
            return;
        }

        if(!handler.addTemplate(argument.trim())){
            botApi.sendMessage(chatId, "Wrong template, try again.");
            return;
        }

        botApi.sendMessage(chatId, "Pattern successfully added.");
    }

}
