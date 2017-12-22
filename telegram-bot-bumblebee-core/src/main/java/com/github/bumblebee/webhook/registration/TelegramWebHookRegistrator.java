package com.github.bumblebee.webhook.registration;

import java.io.File;
import java.io.IOException;

import com.github.telegram.api.Response;
import kotlin.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.telegram.api.BotApi;

@Service
public class TelegramWebHookRegistrator {

    private static final Logger log = LoggerFactory.getLogger(TelegramWebHookRegistrator.class);
    private static final String URL_POSTFIX = "/webhook";

    private final BotApi botApi;

    @Autowired
    public TelegramWebHookRegistrator(BotApi botApi) {
        this.botApi = botApi;
    }

    public boolean registerWebHook(String url, String certificatePath) {
        log.info("Registering webhook: {}, certificate path: {}", url + URL_POSTFIX, certificatePath);
        return setWebHook(url + URL_POSTFIX, certificatePath);
    }

    public boolean removeWebHook() {
        log.info("Removing webhook...");
        return setWebHook("", null);
    }

    private boolean setWebHook(String hookUrl, String filePath) {

        try {
            File certificate = (filePath != null) ? new File(filePath) : null;
            retrofit2.Response<Response<Unit>> callResponse;
            if (filePath != null) {
                callResponse = botApi.setWebhook(hookUrl, certificate).execute();
                Response<Unit> response = callResponse.body();
                log.info("Webhook registration: success = {}, description = {}", response.getOk(),
                        response.getDescription());
                return response.getOk();
            } else {
                botApi.removeWebhook();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to set webhook", e);
        }
    }

}
