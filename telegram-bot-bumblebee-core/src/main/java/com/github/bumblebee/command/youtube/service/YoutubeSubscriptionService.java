package com.github.bumblebee.command.youtube.service;

import com.github.bumblebee.bot.BumblebeeConfig;
import com.github.bumblebee.command.youtube.api.YoutubeSubscriptionApi;
import com.github.bumblebee.command.youtube.dao.YoutubePostedVideosRepository;
import com.github.bumblebee.command.youtube.dao.YoutubeSubscriptionRepository;
import com.github.bumblebee.command.youtube.entity.Chat;
import com.github.bumblebee.command.youtube.entity.PostedVideo;
import com.github.bumblebee.command.youtube.entity.Subscription;
import com.google.api.client.util.Lists;
import feign.Feign;
import feign.Response;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class YoutubeSubscriptionService {

    private static final String CHANNEL_URL = "https://www.youtube.com/xml/feeds/videos.xml?channel_id=";
    private static final String URL_POSTFIX = "/youtube";

    private final YoutubeSubscriptionRepository subscriptionRepository;
    private final YoutubePostedVideosRepository postedVideosRepository;
    private final YoutubeSubscriptionApi youtubeSubscriptionApi;
    private final BumblebeeConfig config;
    private final List<Subscription> subscriptionList;

    @Autowired
    public YoutubeSubscriptionService(YoutubeSubscriptionRepository subscriptionRepository,
                                      YoutubePostedVideosRepository postedVideosRepository,
                                      BumblebeeConfig config) {
        this.config = config;
        this.subscriptionRepository = subscriptionRepository;
        this.postedVideosRepository = postedVideosRepository;
        this.youtubeSubscriptionApi = Feign.builder()
                .logLevel(feign.Logger.Level.BASIC)
                .logger(new Slf4jLogger())
                .target(YoutubeSubscriptionApi.class, YoutubeSubscriptionApi.API_ROOT);
        this.subscriptionList = retrieveSubscriptions();
    }

    public void storeSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    private List<Subscription> retrieveSubscriptions() {
        return Lists.newArrayList(subscriptionRepository.findAll());
    }

    public void deleteSubscription(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

    public boolean subscribeChannel(String channelId) {
        Response response = youtubeSubscriptionApi.subscribe("subscribe", CHANNEL_URL + channelId, config.getUrl() + URL_POSTFIX);
        return response.status() == 202;
    }

    public boolean unsubscribeChannel(String channelId) {
        Response response = youtubeSubscriptionApi.subscribe("unsubscribe", CHANNEL_URL + channelId, config.getUrl() + URL_POSTFIX);
        return response.status() == 202;
    }

    public List<Subscription> getExistingSubscriptions() {
        return subscriptionList;
    }

    public Set<Long> getChatIds(String channelId) {
        Set<Long> idSet = new HashSet<>();
        for (Subscription sub : subscriptionList) {
            if (sub.getChannelId().equals(channelId)) {
                for (Chat chat : sub.getChats()) {
                    idSet.add(chat.getChatId());
                }
            }
        }
        return idSet;
    }

    public void addPostedVideo(PostedVideo video) {
        postedVideosRepository.save(video);
    }

    public List<PostedVideo> retrievePostedVideos() {
        return Lists.newArrayList(postedVideosRepository.findAll());
    }

}
