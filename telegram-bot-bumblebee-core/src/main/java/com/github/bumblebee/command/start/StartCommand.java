package com.github.bumblebee.command.start;

import org.apache.commons.io.IOUtils;
import telegram.api.BotApi;
import telegram.domain.Update;
import telegram.domain.request.ParseMode;
import telegram.polling.UpdateHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Provide info about supported features
 */
public class StartCommand implements UpdateHandler {

    private final BotApi botApi;
    private final String helpText;

    public StartCommand(BotApi botApi) {
        this.botApi = botApi;

        try (InputStream is = StartCommand.class.getClassLoader().getResourceAsStream("start.md")) {
            StringWriter sw = new StringWriter();
            IOUtils.copy(is, sw);
            this.helpText = sw.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onUpdate(Update update) {

        botApi.sendMessage(update.getMessage().getChat().getId(), helpText, ParseMode.MARKDOWN, null,
                update.getMessage().getMessageId(), null);

        return true;
    }
}
