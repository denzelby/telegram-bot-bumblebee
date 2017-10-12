package com.github.bumblebee.webhook.registration;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import telegram.api.BotApi;
import telegram.domain.SetWebHookResponse;
import telegram.domain.request.InputFile;

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

        InputFile inputFile = null;
        if (!StringUtils.isEmpty(filePath)) {
            inputFile = InputFile.document(new File(filePath));
        }

        SetWebHookResponse response = botApi.setWebhook(hookUrl, inputFile);
        log.info("Webhook registration: success = {}, description = {}", response.getResult(),
                response.getDescription());

        return response.getResult() != null && response.getResult();
    }

}
