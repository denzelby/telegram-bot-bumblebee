package com.github.bumblebee;

import feign.Logger;
import feign.slf4j.Slf4jLogger;
import org.slf4j.LoggerFactory;
import telegram.TelegramBot;
import telegram.api.BotApi;
import telegram.api.FileApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class BumblebeeBot {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BumblebeeBot.class);

    public static final String BOT_CONFIG_FILE = "bot.properties";
    private final String token;
    private final Properties config;

    public BumblebeeBot() {

        config = loadConfiguration();
        overrideWithEnvVars(config);
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

    public Properties getConfig() {
        return config;
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

    private void overrideWithEnvVars(Properties config) {

        for (String key : config.stringPropertyNames()) {
            String envVar = key.toUpperCase().replaceAll("\\.", "_");
            String value = System.getenv(envVar);

            if (value != null) {
                log.info("Overriding with environment variable: {}", key);

                config.setProperty(key, value);
            }
        }
    }
}
