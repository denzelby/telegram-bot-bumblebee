package com.github.bumblebee.command.weather;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.utils.BumblebeeUtils;
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
    private final BotApi botApi;
    private final static String URL = "http://meteoinfo.by/radar/UMMN/radar-map.gif";
    private final static String ERROR_MESSAGE = "ехай нахуй";

    private enum WeatherArguments {
        MAP("m"),
        TEMPERATURE("t");
        private final String argument;

        WeatherArguments(String argument) {
            this.argument = argument;
        }

        public String getArgument() {
            return argument;
        }
    }

    @Autowired
    public WeatherCommand(BotApi botApi) {
        this.botApi = botApi;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {
        switch (WeatherArguments.valueOf(argument)) {
            case MAP:
                getWeatherGraphic(chatId);
                break;
            case TEMPERATURE:
                getWeatherTemperature(chatId);
                break;
            default:
                botApi.sendMessage(chatId, ERROR_MESSAGE);
        }
    }

    private void getWeatherGraphic(Long chatId) {
        try {
            InputFile document = InputFile.document(new URL(URL).openStream(), BumblebeeUtils.getFileName(URL));
            botApi.sendDocument(chatId, document);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void getWeatherTemperature(Long chatId) {
        botApi.sendMessage(chatId, "You are not prepared! (me)");
    }
}