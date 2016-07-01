package com.github.bumblebee.webhook;

import com.github.bumblebee.command.youtube.YoutubeUpdateProcessor;
import com.github.bumblebee.webhook.registration.YoutubeWebHookConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
public class YoutubeWebHookController {

    @Autowired
    private YoutubeWebHookConfig config;

    @Autowired
    private YoutubeUpdateProcessor processor;

    final Pattern pattern = Pattern.compile("<yt:videoId>(.+?)</yt:videoId>");

    private Matcher matcher;


    @RequestMapping(method = RequestMethod.POST, path = "/youtube",
            headers = "Content-Type=application/atom+xml", consumes = MediaType.APPLICATION_ATOM_XML_VALUE)
    public void handleUpdates(@RequestBody String s) {

        matcher = pattern.matcher(s);
        if (matcher.find()) {
            this.processor.process(matcher.group(1));
        }

    }

}
