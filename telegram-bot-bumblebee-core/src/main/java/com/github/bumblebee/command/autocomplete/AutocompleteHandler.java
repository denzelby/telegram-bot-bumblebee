package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.ChainedMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.util.*;

/**
 * Created by Misha on 14.05.2016.
 */
@Component
public class AutocompleteHandler extends ChainedMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AutocompleteHandler.class);

    private final BotApi botApi;
    private final AutocompleteConfig config;
    private final Map<String,List<String>> autocomplete = new HashMap<String,List<String>>();

    @Autowired
    public AutocompleteHandler(BotApi botApi, AutocompleteConfig config) {
        this.botApi = botApi;
        this.config = config;
        config.getTemplate().forEach(this::parse);
    }

    private void parse(String phrase){
        List<String> temp = new ArrayList<String>();
        String key = null;

        for (String part: phrase.split(" ")) {
            if (key==null){
                key = part;
            } else {
                temp.add(part);
            }
        }
        autocomplete.put(key, temp);
    }

    @Override
    public boolean onMessage(Long chatId, String message, Update update) {
        try {
            if (autocomplete.containsKey(message)) {
                autocomplete.get(message).forEach(phrase -> botApi.sendMessage(chatId, phrase));
                Thread.sleep(1000);
                return true;
            }
        } catch (InterruptedException e) {
            log.warn("Failed to sleep", e);
        }
        return false;
    }
}
