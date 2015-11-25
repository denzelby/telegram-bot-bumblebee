package com.github.bumblebee.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import telegram.domain.Update;

@RestController
public class WebHookController {

    private static final Logger log = LoggerFactory.getLogger(WebHookController.class);

    @RequestMapping(method = RequestMethod.POST, path = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleUpdates(@RequestBody Update update) {

        log.info("HOOK_UPDATE: {}", update);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public String healthCheck() {
        return "OK";
    }

}
