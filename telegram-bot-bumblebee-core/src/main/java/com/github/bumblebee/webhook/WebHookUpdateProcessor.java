package com.github.bumblebee.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.domain.Update;
import telegram.polling.HandlerRegistry;
import telegram.polling.TelegramUpdateConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class WebHookUpdateProcessor {

    private final TelegramUpdateConsumer updateConsumer;
    private final ExecutorService executor;

    @Autowired
    public WebHookUpdateProcessor(HandlerRegistry handlerRegistry) {
        this.updateConsumer = new TelegramUpdateConsumer(handlerRegistry);
        executor = Executors.newSingleThreadExecutor();
    }

    public void process(Update update) {
        executor.submit(() -> updateConsumer.accept(update));
    }
}
