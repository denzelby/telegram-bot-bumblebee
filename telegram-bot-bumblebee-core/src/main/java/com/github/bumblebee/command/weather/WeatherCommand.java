package com.github.bumblebee.command.weather;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.service.LinkUtils;
import com.github.bumblebee.service.RandomPhraseService;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;
import telegram.domain.request.InputFile;

import java.io.IOException;
import java.net.URL;

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
    public void handleCommand(Update update, Long chatId, String argument) {
        WeatherArgument operation = Optional.fromNullable(WeatherArgument.of(argument))
                .or(WeatherArgument.TEMPERATURE);

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
            InputFile photo = InputFile.photo(new URL(URL).openStream(), "filename.png");
            botApi.sendPhoto(chatId, photo);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendDocumentFromURL(Long chatId, String URL) {
        try {
            InputFile document = InputFile.document(new URL(URL).openStream(), LinkUtils.getFileName(URL));
            botApi.sendDocument(chatId, document);
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