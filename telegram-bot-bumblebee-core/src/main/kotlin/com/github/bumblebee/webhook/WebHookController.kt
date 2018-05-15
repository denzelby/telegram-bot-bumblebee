package com.github.bumblebee.webhook

import com.github.bumblebee.bot.consumer.UpdateProcessor
import com.github.bumblebee.util.logger
import com.github.telegram.domain.Update
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class WebHookController(private val updateProcessor: UpdateProcessor) {

    /**
     * Telegram will send updates to this method after webhook registration
     *
     * @param update https://core.telegram.org/bots/api#update
     */
    @RequestMapping(
        method = [(RequestMethod.POST)], path = ["/webhook"],
        consumes = [(MediaType.APPLICATION_JSON_VALUE)], produces = [(MediaType.APPLICATION_JSON_VALUE)]
    )
    fun handleUpdates(@RequestBody update: Update) {
        log.debug("Webhook update: {}", update.updateId)
        updateProcessor.process(update)
    }

    data class HealthCheckResponse(val status: String)

    @RequestMapping(method = [(RequestMethod.GET)], path = ["/", "/health"])
    fun healthCheck() = HealthCheckResponse("Bumblebee Bot - ok")

    companion object {
        private val log = logger<WebHookController>()
    }
}
