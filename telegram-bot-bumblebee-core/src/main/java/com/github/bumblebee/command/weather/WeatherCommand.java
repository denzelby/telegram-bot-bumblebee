package com.github.bumblebee.command.weather;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.service.RandomPhraseService;
import com.github.telegram.api.BotApi;
import com.github.telegram.domain.Update;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * Created by dzianis.baburkin on 6/16/2016.
 */
@Component
public class WeatherCommand extends SingleArgumentCommand {
    private static final Logger log = LoggerFactory.getLogger(WeatherCommand.class);
    private final static String MAP_URL_DYNAMIC = "http://meteoinfo.by/radar/UMMN/radar-map.gif";
    private final static String MAP_URL_LATEST = "http://meteoinfo.by/radar/UMMN/UMMN_latest.png";
    private final static String TEMPERATURE_URL = "http://www.foreca.ru/meteogram.php?loc_id=100625144&lang=ru";
    private final BotApi botApi;
    private final RandomPhraseService phraseService;

    @Autowired
    public WeatherCommand(BotApi botApi, RandomPhraseService phraseService) {
        this.botApi = botApi;
        this.phraseService = phraseService;
    }

    @Override
    public void handleCommand(Update update, long chatId, String argument) {
        WeatherArgument operation = Optional.ofNullable(WeatherArgument.of(argument))
                .orElse(WeatherArgument.TEMPERATURE);

        switch (operation) {
            case MAP_DYNAMIC:
                sendDocumentFromURL(chatId, MAP_URL_DYNAMIC);
                break;
            case MAP_LATEST:
                sendPhotoFromURL(chatId, MAP_URL_LATEST);
                break;
            case TEMPERATURE:
                sendPhotoFromURL(chatId, TEMPERATURE_URL);
                break;
            default:
                botApi.sendMessage(chatId, phraseService.surprise());
        }
    }

    private void sendPhotoFromURL(Long chatId, String URL) {
        try {
            botApi.sendPhoto(chatId, IOUtils.toByteArray(new URL(URL)), "image/jpeg",
                    null, null, null, null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendDocumentFromURL(Long chatId, String URL) {
        try {
            botApi.sendPhoto(chatId, IOUtils.toByteArray(new URL(URL)), "",
                    null, null, null, null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private enum WeatherArgument {
        MAP_DYNAMIC("md"),
        MAP_LATEST("ml"),
        TEMPERATURE("t");
        private final String argument;

        WeatherArgument(String argument) {
            this.argument = argument;
        }

        public static WeatherArgument of(String code) {

            for (WeatherArgument argument : WeatherArgument.values()) {
                if (argument.getArgument().equalsIgnoreCase(code)) {
                    return argument;
                }
            }
            return null;
        }

        public String getArgument() {
            return argument;
        }
    }
}