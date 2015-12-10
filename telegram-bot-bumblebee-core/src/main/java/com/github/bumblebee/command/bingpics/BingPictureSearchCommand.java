package com.github.bumblebee.command.bingpics;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.bingpics.response.BingSearchData;
import com.github.bumblebee.command.bingpics.response.BingSearchResponse;
import com.github.bumblebee.command.bingpics.response.BingSearchResultItem;
import com.github.bumblebee.service.RandomPhraseService;
import com.google.common.base.Optional;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;
import telegram.domain.request.InputFile;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BingPictureSearchCommand extends SingleArgumentCommand {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BingPictureSearchCommand.class);

    private final BotApi botApi;
    private final BingSearchApi imageSearchApi;
    private final RandomPhraseService randomPhrase;

    @Autowired
    public BingPictureSearchCommand(BotApi botApi, BingSearchConfig config, RandomPhraseService randomPhraseService) {

        this.botApi = botApi;
        this.randomPhrase = randomPhraseService;
        this.imageSearchApi = Feign.builder()
                .decoder(new GsonDecoder())
                .logLevel(Logger.Level.BASIC)
                .logger(new Slf4jLogger())
                .requestInterceptor(new BasicAuthRequestInterceptor("", config.getAccountKey()))
                .target(BingSearchApi.class, BingSearchApi.API_ROOT);
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhrase.surprise());
            return;
        }

        Integer messageId = update.getMessage().getMessageId();
        BingSearchResponse response = imageSearchApi.queryPictures('\'' + argument + '\'');
        BingSearchData data = response.getData();

        if (data != null && data.getResults() != null && !data.getResults().isEmpty()) {
            final List<BingSearchResultItem> pictures = data.getResults();
            sendRandomPicture(pictures, chatId, argument, messageId);
            return;
        } else {
            log.error("Bad Bing response: {}", data);
        }

        botApi.sendMessage(chatId, randomPhrase.no(), messageId);
    }

    private void sendRandomPicture(List<BingSearchResultItem> searchResults, Integer chatId, String argument, Integer messageId) {

        log.info("> {}: {} results", argument, searchResults.size());
        // skip images without extension
        List<BingSearchResultItem> pictures = searchResults.stream()
                .filter(pic -> !FilenameUtils.getExtension(pic.getMediaUrl()).isEmpty())
                .collect(Collectors.toList());
        log.info("{} pictures after filtering", pictures.size());

        Collections.shuffle(pictures);

        String url = null;
        for (BingSearchResultItem picture : pictures) {
            try {
                url = picture.getMediaUrl();
                sendImage(url, chatId, argument);
                return;
            } catch (ImageSendException e) {
                log.error("Image send failed, retrying...", e);

                pictures.remove(picture);
            }
        }

        botApi.sendMessage(chatId, Optional.fromNullable(url).or(randomPhrase.no()), messageId);
    }

    private void sendImage(String url, Integer chatId, String caption) throws ImageSendException {

        log.info("Sending: {}", url);
        try {
            InputFile photo = InputFile.photo(new URL(url).openStream(), getFileName(url));

            botApi.sendPhoto(chatId.toString(), photo, caption);
        } catch (Exception e) {
            log.error("Failed to send: {}", url);
            throw new ImageSendException(e);
        }
    }

    private String getFileName(String url) {

        String name = FilenameUtils.getName(url);
        // if no extension defined - let's guess it
        if (FilenameUtils.getExtension(name).isEmpty()) {
            name += ".jpg";
        } else {
            // skip url params
            int urlParamsIndex = name.indexOf('?');
            if (urlParamsIndex > 0) {
                name = name.substring(0, urlParamsIndex);
            }
        }
        return name;
    }

}
