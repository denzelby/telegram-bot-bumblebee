package com.github.bumblebee.command.youtube;

import com.github.bumblebee.command.youtube.entity.AtomFeed;
import com.github.bumblebee.command.youtube.entity.PostedVideo;
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.telegram.api.BotApi;

import java.util.*;

@Component
public class YoutubeUpdateProcessor {

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";

    private final YoutubeSubscriptionService service;
    private final BotApi botApi;
    private final List<PostedVideo> postedVideos;

    @Autowired
    public YoutubeUpdateProcessor(BotApi botApi, YoutubeSubscriptionService service) {
        this.botApi = botApi;
        this.service = service;
        this.postedVideos = service.retrievePostedVideos();
    }

    public void process(AtomFeed feed) {
        String videoId = feed.getEntry().getVideoId();
        if (!postedVideos.stream().anyMatch(postedVideos -> postedVideos.getVideoId().equals(videoId))) {
            postVideo(feed);
            markVideoAsPosted(videoId);
        }
    }

    private void postVideo(AtomFeed feed) {
        for (Long chatId : service.getChatIds(feed.getEntry().getChannelId())) {
            botApi.sendMessage(chatId, VIDEO_URL + feed.getEntry().getVideoId());
        }
    }

    private void markVideoAsPosted (String videoId) {
        PostedVideo postedVideo = new PostedVideo();
        postedVideo.setVideoId(videoId);
        postedVideo.setPostedDate(new Date());
        postedVideos.add(postedVideo);
        service.addPostedVideo(postedVideo);
    }

}
