package com.github.bumblebee.command.weather;

import com.github.bumblebee.utils.BumblebeeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;
import telegram.domain.request.InputFile;
import telegram.polling.UpdateHandler;

import java.io.IOException;
import java.net.URL;

/**
 * Created by dzianis.baburkin on 6/16/2016.
 */

@Component
public class WeatherCommand implements UpdateHandler {
    private static final Logger log = LoggerFactory.getLogger(WeatherCommand.class);
    private final BotApi botApi;
    private final static String URL = "http://meteoinfo.by/radar/UMMN/radar-map.gif";

    @Autowired
    public WeatherCommand(BotApi botApi) {
        this.botApi = botApi;
    }

    @Override
    public boolean onUpdate(Update update) {
        try {
            Long chatId = update.getMessage().getChat().getId();
            InputFile document = InputFile.document(new URL(URL).openStream(), BumblebeeUtils.getFileName(URL));
            botApi.sendDocument(chatId, document);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }
}