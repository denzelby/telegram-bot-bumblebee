package com.github.bumblebee.command.youtube;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.service.RandomPhraseService;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.io.IOException;
import java.util.List;

@Component
public class YoutubeSearchCommand extends SingleArgumentCommand {

    private static final Logger log = LoggerFactory.getLogger(YoutubeSearchCommand.class);

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final Long NUMBER_OF_VIDEOS_RETURNED = 1L;

    private final BotApi botApi;
    private final YouTube youtube;
    private final String googleApiKey;
    private RandomPhraseService randomPhraseService;

    @Autowired
    public YoutubeSearchCommand(BotApi botApi, RandomPhraseService randomPhraseService, YoutubeSearchConfig config) {

        if (!config.isAvailable()) {
            throw new IllegalStateException("");
        }

        this.botApi = botApi;
        this.randomPhraseService = randomPhraseService;
        this.googleApiKey = config.getKey();

        this.youtube = new YouTube
                .Builder(new ApacheHttpTransport(), new JacksonFactory(), req -> {})
                .setApplicationName(config.getProjectName())
                .build();
    }

    @Override
    public void handleCommand(Update update, Integer chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise());
            return;
        }

        try {
            String videoId = searchVideo(argument);
            if (videoId != null) {
                botApi.sendMessage(chatId, VIDEO_URL + videoId);
                return;
            }
        } catch (IOException e) {
            log.error("Error during youtube search", e);
        }
        botApi.sendMessage(chatId, randomPhraseService.no(), update.getMessage().getMessageId());
    }

    private String searchVideo(String searchQuery) throws IOException {

        // Define the API request for retrieving search results.
        YouTube.Search.List search = youtube.search().list("id");

        search.setKey(googleApiKey);
        search.setQ(searchQuery);

        // Restrict the search results to only include videos.
        search.setType("video");

        // To increase efficiency, only retrieve the fields that the application uses.
        search.setFields("items(id/videoId)");
        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

        // Call the API and print results.
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        if (searchResultList != null && !searchResultList.isEmpty()) {
            return searchResultList.get(0).getId().getVideoId();
        }
        return null;
    }

    private void checkGoogleApiCredentials(String googleAppName, String googleApiKey) {
        if (googleAppName == null) {
            throw new IllegalStateException("Google project name is not defined!");
        }
        if (googleApiKey == null) {
            throw new IllegalStateException("Google api key is not defined!");
        }
    }
}
