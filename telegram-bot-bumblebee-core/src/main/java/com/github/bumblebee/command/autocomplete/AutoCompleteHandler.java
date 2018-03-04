package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.ChainedMessageListener;
import com.github.bumblebee.command.autocomplete.entity.AutoCompletePhrase;
import com.github.bumblebee.command.autocomplete.service.AutoCompleteService;
import com.github.telegram.api.BotApi;
import com.github.telegram.domain.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class AutoCompleteHandler extends ChainedMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AutoCompleteHandler.class);

    private final BotApi botApi;
    private final AutoCompleteService service;
    private final Map<String, String[]> autocompletes;
    private static final Pattern pattern = Pattern.compile("[^\\/\\n]+\\/([^\\/\\n]+\\/)*[^\\/\\n]+");

    @Autowired
    public AutoCompleteHandler(BotApi botApi, AutoCompleteService service) {
        this.botApi = botApi;
        this.service = service;
        autocompletes = service.retrieveAutoCompletePhrases().stream()
                .collect(Collectors
                        .toMap(key -> key.getPhraseKey(),
                                value -> value.getPhrasePattern().split("/")));
    }

    // TODO: create good parser
    public boolean addTemplate(String phraseTemplate) {
        if (isValidTemplate(phraseTemplate)) {
            String patternKey = phraseTemplate.substring(0, phraseTemplate.indexOf('/'));
            String patternPhrase = phraseTemplate.replaceFirst(Pattern.quote(patternKey + "/"), "");
            String[] patternValue = patternPhrase.split("/");
            if (autocompletes.containsKey(patternKey))
                service.updateAutoCompletePhrase(patternKey, patternPhrase);
            else
                service.storeAutoCompletePhrase(new AutoCompletePhrase(patternKey, patternPhrase));
            autocompletes.put(patternKey, patternValue);
            return true;
        }
        return false;
    }

    private boolean isValidTemplate(String argument) {
        return pattern.matcher(argument).matches();
    }

    public Set<String> getAutoCompletes() {
        return autocompletes.keySet();
    }

    @Override
    public boolean onMessage(long chatId, String message, Update update) {
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


