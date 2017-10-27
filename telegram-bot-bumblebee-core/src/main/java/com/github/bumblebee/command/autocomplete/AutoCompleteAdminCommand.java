package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.security.PrivilegedCommand;
import com.github.bumblebee.security.UnauthorizedRequestAware;
import com.github.bumblebee.security.UserRole;
import com.github.bumblebee.service.RandomPhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import telegram.api.BotApi;
import telegram.domain.Update;


@PrivilegedCommand(name = "AutoCompleteAdminCommand", role = UserRole.MODERATOR)
public class AutoCompleteAdminCommand extends SingleArgumentCommand implements UnauthorizedRequestAware {

    private final BotApi botApi;
    private final AutoCompleteHandler handler;
    private final RandomPhraseService randomPhraseService;

    @Autowired
    public AutoCompleteAdminCommand(BotApi botApi, AutoCompleteHandler handler,
                                    RandomPhraseService randomPhraseService) {
        this.botApi = botApi;
        this.handler = handler;
        this.randomPhraseService = randomPhraseService;
    }

    @Override
    public void handleCommand(Update update, long chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise());
            return;
        }

        if (!handler.addTemplate(argument.trim())) {
            botApi.sendMessage(chatId, "Wrong template, try again.");
            return;
        }

        botApi.sendMessage(chatId, "Pattern successfully added.");
    }

    @Override
    public void onUnauthorizedRequest(Update update) {
        botApi.sendMessage(update.getMessage().getChat().getId(),
                "You are not allowed to execute this command. Become a moderator to do this.");
    }
}
