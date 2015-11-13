package com.github.bumblebee;

import feign.Logger;
import feign.slf4j.Slf4jLogger;
import telegram.TelegramBot;
import telegram.api.BotApi;
import telegram.api.FileApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class BumblebeeBot {

    public static final String BOT_CONFIG_FILE = "bot.properties";
    private final String token;

    public BumblebeeBot() {

        Properties config = loadConfiguration();
        token = config.getProperty("bumblebee.token");
    }

    public BotApi create() {

        return new TelegramBot(token)
                .withLogLevel(Logger.Level.BASIC)
                .withLogger(new Slf4jLogger())
                .create();
    }

    public FileApi createFileApi() {

        return TelegramBot.createFileApi(token);
    }

    private Properties loadConfiguration() {
        try (InputStream is = BumblebeeBot.class.getClassLoader().getResourceAsStream(BOT_CONFIG_FILE)) {
            Properties botConfig = new Properties();
            botConfig.load(is);
            return botConfig;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read bot configuration");
        }
    }
}
