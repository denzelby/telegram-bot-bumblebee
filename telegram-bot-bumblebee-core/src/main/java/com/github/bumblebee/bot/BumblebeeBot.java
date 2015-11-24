package com.github.bumblebee.bot;

import feign.Logger;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import telegram.TelegramBot;
import telegram.api.BotApi;
import telegram.api.FileApi;

@Configuration
public class BumblebeeBot {

    @Autowired
    private BumblebeeConfig config;

    @Bean
    public BotApi create() {
        return new TelegramBot(config.getToken())
                .withLogLevel(Logger.Level.BASIC)
                .withLogger(new Slf4jLogger())
                .create();
    }

    @Bean
    public FileApi createFileApi() {
        return TelegramBot.createFileApi(config.getToken());
    }

}
