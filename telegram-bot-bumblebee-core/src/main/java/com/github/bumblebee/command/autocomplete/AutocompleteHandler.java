package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.ChainedMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Component
public class AutocompleteHandler extends ChainedMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AutocompleteHandler.class);

    private final BotApi botApi;
    private final Map<String, String[]> autocompletes = new HashMap<>();

    @Autowired
    public AutocompleteHandler(BotApi botApi, AutocompleteConfig config) {
        this.botApi = botApi;

        config.getTemplates().forEach(this::addTemplate);
    }

    public boolean addTemplate(String phraseTemplate) {
        if (isValidTemplate(phraseTemplate)) {
            String patternKey = phraseTemplate.substring(0, phraseTemplate.indexOf('/'));
            String[] patternValue = phraseTemplate.replaceFirst(Pattern.quote(patternKey + "/"), "").split("/");
            autocompletes.put(patternKey, patternValue);
            return true;
        }
        return false;
    }

    private boolean isValidTemplate(String argument) {
        if (argument == null ||
                !argument.contains("/") ||
                argument.substring(0, argument.indexOf('/')).length() + 1 >= argument.length()) {

    private boolean checkArgument(String argument) {
        if (!argument.matches(Pattern.compile("[^\\/\\n]+\\/([^\\/\\n]+\\/)*[^\\/\\n]+").toString())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onMessage(Long chatId, String message, Update update) {
        try {
            if (autocompletes.containsKey(message)) {
                for (String text : autocompletes.get(message)) {
                    if (text.startsWith("stickerId:")) {
                        botApi.sendSticker(chatId, text.replace("stickerId:", ""));
                    } else {
                        botApi.sendMessage(chatId, text);
                    }
                    Thread.sleep(400);
                }
                return true;
            }
        } catch (InterruptedException e) {
            log.warn("Failed to sleep", e);
        }
        return false;
    }
}


