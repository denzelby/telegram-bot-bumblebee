package com.github.bumblebee.command.youtube;

import com.github.bumblebee.command.youtube.entity.AtomFeed;
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class YoutubeUpdateProcessor {

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final long CLEAN_DELAY = 60 * 1000 * 60 * 4;
    private static final long UPDATE_DELAY = TimeUnit.HOURS.toMillis(3);
    private static final long OVERDUE_DELAY = TimeUnit.HOURS.toMillis(6);
    private final YoutubeSubscriptionService service;
    private final YoutubeSubscribeCommand command;
    private final BotApi botApi;
    private final Map<String, Date> postedVideos = new HashMap<>();

    @Autowired
    public YoutubeUpdateProcessor(BotApi botApi, YoutubeSubscriptionService service, YoutubeSubscribeCommand command) {

        this.botApi = botApi;
        this.service = service;
        this.command = command;

    }

    public Map<String, Date> getPostedVideos() {
        return postedVideos;
    }

    public void process(AtomFeed feed) {
        String videoId = feed.getEntry().getVideoId();
        if (!postedVideos.containsKey(videoId)) {
            postVideo(feed);
            postedVideos.put(videoId, feed.getEntry().getUpdated());
        } else {
            Date date = new Date();
            long interval = date.getTime() - postedVideos.get(videoId).getTime();
            if (interval > UPDATE_DELAY) {
                postVideo(feed);
                postedVideos.put(videoId, feed.getEntry().getUpdated());
            }
        }
    }

    private void postVideo(AtomFeed feed) {
        for (Long chatId : command.getChatIds(feed.getEntry().getChannelId())) {
            botApi.sendMessage(chatId, VIDEO_URL + feed.getEntry().getVideoId());
        }
    }

    @Scheduled(fixedRate = CLEAN_DELAY)
    private void cleanOverdueMapEntries() {
        Date date = new Date();
        for (Iterator<Map.Entry<String, Date>> it = postedVideos.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Date> entry = it.next();
            if (date.getTime() - entry.getValue().getTime() > OVERDUE_DELAY)
                it.remove();
        }
    }

}
