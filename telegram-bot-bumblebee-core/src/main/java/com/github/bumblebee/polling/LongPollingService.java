package com.github.bumblebee.polling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegram.api.BotApi;
import telegram.polling.HandlerRegistry;
import telegram.polling.TelegramUpdateAction;
import telegram.polling.TelegramUpdateService;

@Service
public class LongPollingService {

    private TelegramUpdateService updateService;

    @Autowired
    public LongPollingService(BotApi botApi, HandlerRegistry handlerRegistry) {

        updateService = new TelegramUpdateService(new TelegramUpdateAction(botApi, handlerRegistry));
    }

    public void startPolling() {
        updateService.startPolling();
    }
}
