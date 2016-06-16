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
    private final static String URL = "http://meteoinfo.by/radar/UMMN/radar-map.gif";
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
                .or(WeatherArgument.MAP);

        switch (operation) {
            case MAP:
                getMeteoRadar(chatId);
                break;
            case TEMPERATURE:
                getWeatherTemperature(chatId);
                break;
            default:
                botApi.sendMessage(chatId, phraseService.surprise());
        }
    }

    private void getMeteoRadar(Long chatId) {
        try {
            InputFile document = InputFile.document(new URL(URL).openStream(), LinkUtils.getFileName(URL));
            botApi.sendDocument(chatId, document);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void getWeatherTemperature(Long chatId) {
        botApi.sendMessage(chatId, "You are not prepared! (me)");
    }

    private enum WeatherArgument {
        MAP("m"),
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