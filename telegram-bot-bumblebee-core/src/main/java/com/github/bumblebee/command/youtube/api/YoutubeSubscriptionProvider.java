package com.github.bumblebee.command.youtube.api;

import com.github.bumblebee.bot.BumblebeeConfig;
import feign.Client;
import feign.Feign;
import feign.Response;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Service
public class YoutubeSubscriptionProvider {

    private final YoutubeSubscriptionApi youtubeSubscriptionApi;

    @Autowired
    private BumblebeeConfig config;

    private static final String CHANNEL_URL = "https://www.youtube.com/xml/feeds/videos.xml?channel_id=";

    public YoutubeSubscriptionProvider() {
        this.youtubeSubscriptionApi = Feign.builder()
                .logLevel(feign.Logger.Level.BASIC)
                .logger(new Slf4jLogger())
                .target(YoutubeSubscriptionApi.class, YoutubeSubscriptionApi.API_ROOT);

    }

    public Boolean subscribeChannel(String channelId) {

        Response response = youtubeSubscriptionApi.subscribe("subscribe", CHANNEL_URL + channelId, config.getUrl() + "/youtube");
        if (response.status() == 202)
            return true;
        else return false;

    }

    public boolean unsubscribeChannel(String channelId) {
        Response response = youtubeSubscriptionApi.subscribe("unsubscribe", CHANNEL_URL + channelId, config.getUrl() + "/youtube");
        if (response.status() == 202)
            return true;
        else return false;
    }

}
