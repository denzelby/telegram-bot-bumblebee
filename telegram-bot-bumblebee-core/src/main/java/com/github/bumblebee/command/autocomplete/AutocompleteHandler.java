package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.ChainedMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

/**
 * Created by Misha on 14.05.2016.
 */
@Component
public class AutocompleteHandler extends ChainedMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AutocompleteHandler.class);

    private final BotApi botApi;

    @Autowired
    public AutocompleteHandler(BotApi botApi) {
        this.botApi = botApi;
    }

    @Override
    public boolean onMessage(Long chatId, String message, Update update) {

        try {
            if (message.equalsIgnoreCase("ДО")) {
                botApi.sendMessage(chatId, "СВИ");
                Thread.sleep(1000);
                botApi.sendMessage(chatId, "ДОС");
                return true;
            }
            if (message.equalsIgnoreCase("ДОС")) {
                botApi.sendMessage(chatId, "ВИ");
                Thread.sleep(1000);
                botApi.sendMessage(chatId, "ДОС");
                return true;
            }
        } catch (InterruptedException e) {
            log.warn("Failed to sleep", e);
        }
        return false;
    }
}
