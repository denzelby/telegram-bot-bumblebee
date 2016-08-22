package com.github.bumblebee.webhook.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegram.api.BotApi;
import telegram.domain.SetWebHookResponse;

@Service
public class TelegramWebHookRegistrator {

    private static final Logger log = LoggerFactory.getLogger(TelegramWebHookRegistrator.class);

    private final BotApi botApi;

    @Autowired
    public TelegramWebHookRegistrator(BotApi botApi) {
        this.botApi = botApi;
    }

    public boolean registerWebHook(String url) {
        log.info("Registering webhook: {}", url + "/webhook");
        return setWebHook(url + "/webhook");
    }

    public boolean removeWebHook() {
        log.info("Removing webhook...");
        return setWebHook("");
    }

    private boolean setWebHook(String hookUrl) {

        SetWebHookResponse response = botApi.setWebhook(hookUrl);
        log.info("Webhook registration: success = {}, description = {}", response.getResult(),
                response.getDescription());

        return response.getResult() != null && response.getResult();
    }

}
