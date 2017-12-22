package com.github.bumblebee.webhook;

import com.github.bumblebee.bot.BumblebeeConfig;
import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.github.telegram.domain.Update;

@RestController
public class WebHookController {

    private static final Logger log = LoggerFactory.getLogger(WebHookController.class);

    @Autowired
    private WebHookUpdateProcessor updateProcessor;

    @Autowired
    private TelegramWebHookRegistrator hookRegistrator;

    @Autowired
    private BumblebeeConfig bumblebeeConfig;

    /**
     * Telegram will send updates to this method after webhook registration
     *
     * @param update https://core.telegram.org/bots/api#update
     */
    @RequestMapping(method = RequestMethod.POST, path = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleUpdates(@RequestBody Update update) {

        log.debug("Webhook update: {}", update.getUpdateId());

        this.updateProcessor.process(update);
    }

    /**
     * Manually re-set web hook url
     *
     * @return true if webhook successfully set
     */
    @RequestMapping(method = RequestMethod.GET, path = "/bind", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean bindWebHook() {
        return hookRegistrator.registerWebHook(bumblebeeConfig.getUrl(), bumblebeeConfig.getCertificatePath());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public String healthCheck() {
        return "OK";
    }

}
