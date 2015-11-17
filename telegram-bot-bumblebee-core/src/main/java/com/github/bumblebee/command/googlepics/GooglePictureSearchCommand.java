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
import java.util.Collections;
import java.util.List;

public class GooglePictureSearchCommand extends SingleArgumentCommand {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GooglePictureSearchCommand.class);
    private static final String DEFAULT_IMAGE_EXTENSION = ".jpg";

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

        Integer messageId = update.getMessage().getMessageId();
        GooglePicsResponse response = googlePicsApi.queryPictures(argument);
        List<GooglePicsResponse.PictureResult> pictures = response.getResponseData().getResults();
        log.debug("{} results for {}", pictures.size(), argument);

        if (!pictures.isEmpty()) {
            // botApi.sendMessage(chatId, pictures.get(0).getUrl());
            boolean isSent = sendAsPicture(selectPicture(pictures), chatId, argument, messageId);
            if (isSent) {
                return;
            }
            log.debug("Picture send failed for argument: {}", argument);
        }


        botApi.sendMessage(chatId, randomPhrase.no(), messageId);
    }

    private GooglePicsResponse.PictureResult selectPicture(List<GooglePicsResponse.PictureResult> pictures) {

        Collections.shuffle(pictures);
        // prefer pictures with extensions
        return pictures.stream()
                .filter(pic -> !FilenameUtils.getExtension(pic.getUrl()).isEmpty())
                .findAny()
                .orElseGet(() -> pictures.get(0));
    }

    private boolean sendAsPicture(GooglePicsResponse.PictureResult pictureResult, Integer chatId, String argument, Integer messageId) {

        InputFile photo = null;
        String url = pictureResult.getUrl();
        try {
            photo = InputFile.photo(new URL(url).openStream(), getFileName(url));
        } catch (IOException e) {
            log.error("Error during picture download", e);
        }

        if (photo != null) {
            try {
                botApi.sendPhoto(chatId.toString(), photo, argument);
            } catch (Exception e) {
                log.error("Error sending photo", e);
                // try send at least url
                botApi.sendMessage(chatId, url, messageId);
            }
            return true;
        }
        return false;
    }

    private String getFileName(String url) {

        String name = FilenameUtils.getName(url);
        // if no extension defined - let's guess it
        if (FilenameUtils.getExtension(name).isEmpty()) {
            name += DEFAULT_IMAGE_EXTENSION;
        }
        return name;
    }
}
