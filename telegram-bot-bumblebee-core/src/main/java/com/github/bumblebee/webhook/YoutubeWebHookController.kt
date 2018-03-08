package com.github.bumblebee.webhook

import com.github.bumblebee.command.youtube.YoutubeUpdateProcessor
import com.github.bumblebee.command.youtube.service.AtomParser
import com.github.bumblebee.util.logger
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class YoutubeWebHookController(private val processor: YoutubeUpdateProcessor,
                               private val parser: AtomParser) {

    @RequestMapping(method = [(RequestMethod.POST)], path = ["/youtube"],
            headers = ["Content-Type=application/atom+xml"], consumes = [(MediaType.APPLICATION_ATOM_XML_VALUE)])
    fun handleUpdates(@RequestBody message: String) {
        log.info("Received atom feed: {}", message)

        val video = parser.parse(message)
        if (video != null) {
            processor.process(video)
        } else {
            log.warn("Failed to parse atom feed: {}", message)
        }
    }

    @RequestMapping(method = [(RequestMethod.GET)], path = ["/youtube"], produces = [(MediaType.TEXT_PLAIN_VALUE)])
    fun confirmSubscription(@RequestParam("hub.mode") mode: String,
                            @RequestParam("hub.topic") topic: String,
                            @RequestParam("hub.challenge") challenge: String,
                            @RequestParam("hub.lease_seconds") leaseSeconds: Int?): String {
        return challenge
    }

    companion object {
        val log = logger<YoutubeUpdateProcessor>()
    }
}
