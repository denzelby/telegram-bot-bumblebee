package com.github.bumblebee.webhook

import com.github.bumblebee.bot.BumblebeeConfig
import com.github.bumblebee.util.loggerFor
import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator
import com.github.telegram.domain.Update
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
class WebHookController(private val updateProcessor: WebHookUpdateProcessor,
                        private val hookRegistrator: TelegramWebHookRegistrator,
                        private val bumblebeeConfig: BumblebeeConfig) {

    /**
     * Telegram will send updates to this method after webhook registration
     *
     * @param update https://core.telegram.org/bots/api#update
     */
    @RequestMapping(method = [(RequestMethod.POST)], path = ["/webhook"],
            consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun handleUpdates(@RequestBody update: Update) {

        log.debug("Webhook update: {}", update.updateId)

        this.updateProcessor.process(update)
    }

    /**
     * Manually re-set web hook url
     *
     * @return true if webhook successfully set
     */
    @RequestMapping(method = [(RequestMethod.GET)], path = ["/bind"])
    fun bindWebHook(): Boolean {
        return if (bumblebeeConfig.url.isNullOrBlank()) {
            hookRegistrator.registerWebHook(bumblebeeConfig.url!!, bumblebeeConfig.certificatePath)
        } else false
    }

    @RequestMapping(method = [(RequestMethod.GET)], path = ["/health"])
    fun healthCheck(): String {
        return "OK"
    }

    companion object {
        private val log = loggerFor<WebHookController>()
    }

}
