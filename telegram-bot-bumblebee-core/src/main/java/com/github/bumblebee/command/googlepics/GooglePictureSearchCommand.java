package com.github.bumblebee.command.googlepics;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.service.RandomPhraseService;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import telegram.api.BotApi;
import telegram.domain.Update;
import telegram.domain.request.InputFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GooglePictureSearchCommand extends SingleArgumentCommand {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GooglePictureSearchCommand.class);

    private final BotApi botApi;
    private final GooglePicsApi googlePicsApi;
    private final RandomPhraseService randomPhrase = new RandomPhraseService();

    public GooglePictureSearchCommand(BotApi botApi) {

        this.botApi = botApi;
        googlePicsApi = Feign.builder()
                .decoder(new GsonDecoder())
                .logLevel(Logger.Level.BASIC)
                .logger(new Slf4jLogger())
                .target(GooglePicsApi.class, "https://ajax.googleapis.com/ajax/services");
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhrase.surprise());
            return;
        }

        GooglePicsResponse response = googlePicsApi.queryPictures(argument);
        List<GooglePicsResponse.PictureResult> pictures = response.getResponseData().getResults();
        log.debug("{} results for {}", pictures.size(), argument);
        if (!pictures.isEmpty()) {
            // botApi.sendMessage(chatId, pictures.get(0).getUrl());
            boolean isSent = sendAsPicture(pictures.get(0), chatId, argument);
            if (isSent) {
                return;
            }
        }

        botApi.sendMessage(chatId, randomPhrase.no(), update.getMessage().getMessageId());
    }

    private boolean sendAsPicture(GooglePicsResponse.PictureResult pictureResult, Integer chatId, String argument) {
        try {
            String url = pictureResult.getUrl();
            InputFile photo = InputFile.photo(new URL(url).openStream(), FilenameUtils.getName(url));
            botApi.sendPhoto(chatId.toString(), photo, argument);
            return true;
        } catch (IOException e) {
            log.error("Error during picture download", e);
        }
        return false;
    }
}
