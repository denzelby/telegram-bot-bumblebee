package com.github.bumblebee.webhook

import com.github.bumblebee.command.youtube.YoutubeUpdateProcessor
import com.github.bumblebee.command.youtube.service.AtomParser
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class YoutubeWebHookController(private val processor: YoutubeUpdateProcessor,
                               private val parser: AtomParser) {

    @RequestMapping(method = [(RequestMethod.POST)], path = ["/youtube"],
            headers = ["Content-Type=application/atom+xml"], consumes = [(MediaType.APPLICATION_ATOM_XML_VALUE)])
    fun handleUpdates(@RequestBody message: String) {
        this.processor.process(parser.parse(message))
    }

    @RequestMapping(method = [(RequestMethod.GET)], path = ["/youtube"], produces = [(MediaType.TEXT_PLAIN_VALUE)])
    fun confirmSubscription(@RequestParam("hub.mode") mode: String,
                            @RequestParam("hub.topic") topic: String,
                            @RequestParam("hub.challenge") challenge: String,
                            @RequestParam("hub.lease_seconds") leaseSeconds: Int?): String {
        return challenge
    }

}
