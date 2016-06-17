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
import java.util.Set;
import java.util.regex.Pattern;


@Component
public class AutoCompleteHandler extends ChainedMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AutoCompleteHandler.class);

    private final BotApi botApi;
    private final Map<String, String[]> autocompletes = new HashMap<>();
    private static final Pattern pattern = Pattern.compile("[^\\/\\n]+\\/([^\\/\\n]+\\/)*[^\\/\\n]+");

    @Autowired
    public AutoCompleteHandler(BotApi botApi, AutoCompleteConfig config) {
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
        return pattern.matcher(argument).matches();
    }

    public Set<String> getAutoCompletes(){
        return autocompletes.keySet();
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
            log.debug("Autocomplete handler interrupted", e);
        }
        return false;
    }
}


