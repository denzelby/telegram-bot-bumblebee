package com.github.bumblebee.webhook;

import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator;
import com.github.bumblebee.webhook.registration.WebHookConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import telegram.domain.Update;
import telegram.polling.HandlerRegistry;
import telegram.polling.TelegramUpdateConsumer;

import javax.annotation.PostConstruct;

@RestController
public class WebHookController {

    private static final Logger log = LoggerFactory.getLogger(WebHookController.class);

    @Autowired
    private HandlerRegistry handlerRegistry;

    private TelegramUpdateConsumer updateConsumer;

    @Autowired
    private TelegramWebHookRegistrator hookRegistrator;

    @Autowired
    private WebHookConfig hookConfig;

    @PostConstruct
    public void init() {
        this.updateConsumer = new TelegramUpdateConsumer(handlerRegistry);
    }

    /**
     * Telegram will send updates to this method
     * @param update
     */
    @RequestMapping(method = RequestMethod.POST, path = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleUpdates(@RequestBody Update update) {

        log.debug("Webhook update: {}", update.getUpdateId());

        this.updateConsumer.accept(update);
    }

    /**
     * Manually re-set web hook url
     * @return true if webhook successfully set
     */
    @RequestMapping(method = RequestMethod.GET, path = "/bind", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean bindWebHook() {
        return hookRegistrator.registerWebHook(hookConfig.getUrl());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public String healthCheck() {
        return "OK";
    }

}
