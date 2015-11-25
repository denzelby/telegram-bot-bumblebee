package com.github.bumblebee.bot;

import com.github.bumblebee.polling.LongPollingService;
import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator;
import com.github.bumblebee.webhook.registration.WebHookConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BumblebeeUpdateService {

    private static final Logger log = LoggerFactory.getLogger(BumblebeeUpdateService.class);

    private LongPollingService longPollingService;
    private WebHookConfig webHookConfig;
    private TelegramWebHookRegistrator webHookRegistrator;

    @Autowired
    public BumblebeeUpdateService(LongPollingService longPollingService, WebHookConfig webHookConfig,
                                  TelegramWebHookRegistrator webHookRegistrator) {
        this.longPollingService = longPollingService;
        this.webHookConfig = webHookConfig;
        this.webHookRegistrator = webHookRegistrator;
    }

    @PostConstruct
    public void init() {
        start();
    }

    public void start() {
        if (webHookConfig.isEnabled()) {
            webHookRegistrator.registerWebHook(webHookConfig.getUrl());
        } else {
            webHookRegistrator.removeWebHook();
            longPollingService.startPolling();
            log.info("Started polling service");
        }
    }
}
