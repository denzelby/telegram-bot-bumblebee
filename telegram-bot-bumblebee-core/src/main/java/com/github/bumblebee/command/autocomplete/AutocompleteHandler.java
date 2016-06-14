package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.ChainedMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Misha on 14.05.2016.
 */
@Component
public class AutocompleteHandler extends ChainedMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AutocompleteHandler.class);

    private final BotApi botApi;
    private final AutocompleteConfig config;
    final Map<String, String[]> autocompletes;

    @Autowired
    public AutocompleteHandler(BotApi botApi, AutocompleteConfig config) {
        this.botApi = botApi;
        this.config = config;
        autocompletes = config.getTemplates().stream()
                .map(phrase -> phrase.split("/"))
                .collect(Collectors.toMap(
                        syllables -> syllables[0],
                        syllables -> Arrays.copyOfRange(syllables, 1, syllables.length))
                );
    }

    @Override
    public boolean onMessage(Long chatId, String message, Update update) {
        try {
            if (autocompletes.containsKey(message)) {
                for (String text: autocompletes.get(message)){
                    if(text.startsWith("stickerId:")){
                        botApi.sendSticker(chatId.toString(), text.replace("stickerId:",""));
                        Thread.sleep(400);
                    }
                    else {
                        botApi.sendMessage(chatId, text);
                        Thread.sleep(400);
                    }
                }
                return true;
            }
        } catch (InterruptedException e) {
            log.warn("Failed to sleep", e);
        }
        return false;
    }
}


