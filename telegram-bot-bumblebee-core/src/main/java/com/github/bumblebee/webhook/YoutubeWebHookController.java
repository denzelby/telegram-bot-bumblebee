package com.github.bumblebee.webhook;

import com.github.bumblebee.command.youtube.YoutubeUpdateProcessor;
import com.github.bumblebee.command.youtube.entity.AtomFeed;
import com.github.bumblebee.command.youtube.service.AtomParser;
import com.github.bumblebee.command.youtube.service.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;


@RestController
public class YoutubeWebHookController {


    @Autowired
    private YoutubeUpdateProcessor processor;

    private XmlParser parser = new AtomParser();


    @RequestMapping(method = RequestMethod.POST, path = "/youtube",
            headers = "Content-Type=application/atom+xml", consumes = MediaType.APPLICATION_ATOM_XML_VALUE)
    public void handleUpdates(@RequestBody String message) {
        try {
            this.processor.process((AtomFeed) parser.getObject(message, AtomFeed.class));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(method = RequestMethod.GET, path = "/youtube", produces = MediaType.TEXT_PLAIN_VALUE)
    public String confirmSubscription(@RequestParam("hub.mode") String mode,
                                      @RequestParam("hub.topic") String topic,
                                      @RequestParam("hub.challenge") String challenge,
                                      @RequestParam("hub.lease_seconds") Integer leaseSeconds) {
        return challenge;
    }

}
