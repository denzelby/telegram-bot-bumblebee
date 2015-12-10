package com.github.bumblebee.command.bingpics;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.service.RandomPhraseService;
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

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

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
                .target(BingSearchApi.class, "https://api.datamarket.azure.com/Bing/Search/v1");
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhrase.surprise());
            return;
        }

        Integer messageId = update.getMessage().getMessageId();
        BingSearchResponse response = imageSearchApi.queryPictures('\'' + argument + '\'');
        BingSearchResponse.BingSearchData data = response.getData();

        if (data != null && data.getResults() != null && !data.getResults().isEmpty()) {
            final List<BingSearchResponse.BingSearchResult> pictures = data.getResults();
            log.debug("{} results for {}", pictures.size(), argument);
            // botApi.sendMessage(chatId, pictures.get(0).getUrl());
            boolean isSent = sendAsPicture(selectPicture(pictures).getMediaUrl(), chatId, argument, messageId);
            if (isSent) {
                return;
            }
            log.debug("Picture send failed for argument: {}", argument);
        }

        botApi.sendMessage(chatId, randomPhrase.no(), messageId);
    }

    private BingSearchResponse.BingSearchResult selectPicture(List<BingSearchResponse.BingSearchResult> pictures) {

        Collections.shuffle(pictures);
        // prefer pictures with extensions
        return pictures.stream()
                .filter(pic -> !FilenameUtils.getExtension(pic.getMediaUrl()).isEmpty())
                .findAny()
                .orElseGet(() -> pictures.get(0));
    }

    private boolean sendAsPicture(String url, Integer chatId, String argument, Integer messageId) {

        InputFile photo = null;
        try {
            photo = InputFile.photo(new URL(url).openStream(), getFileName(url));
        } catch (IOException e) {
            log.error("Error during picture download", e);
            botApi.sendMessage(chatId, url, messageId);
        }

        if (photo != null) {
            try {
                botApi.sendPhoto(chatId.toString(), photo, argument);
            } catch (Exception e) {
                log.error("Error sending photo", e);
                // try send at least url
                botApi.sendMessage(chatId, url, messageId);
            }
        }
        return true;
    }

    private String getFileName(String url) {

        String name = FilenameUtils.getName(url);
        // if no extension defined - let's guess it
        if (FilenameUtils.getExtension(name).isEmpty()) {
            name += ".jpg";
        }
        return name;
    }

    public static void main(String[] args) {
        final BingSearchConfig cfg = new BingSearchConfig();
        cfg.setAccountKey("VugNVj0MWwXMI9HhqkLSrXdTf6Ps5b/C5lrkz3Y57m8=");

        final BingPictureSearchCommand cmd = new BingPictureSearchCommand(null, cfg, null);
        cmd.handleCommand(null, null, "audi a4 b8");
    }

}
