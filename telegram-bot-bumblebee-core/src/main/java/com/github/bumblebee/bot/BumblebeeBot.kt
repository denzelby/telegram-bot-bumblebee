package com.github.bumblebee.bot

import com.github.bumblebee.polling.LongPollingService
import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class BumblebeeBot(private val config: BumblebeeConfig,
                   private val webHookRegistrator: TelegramWebHookRegistrator,
                   private val longPollingService: LongPollingService) {

    @PostConstruct
    fun start() {
        if (config.webhook.enabled && config.url.isNotEmpty()) {
            webHookRegistrator.registerWebHook(config.url, config.certificatePath)
        } else {
            webHookRegistrator.removeWebHook()
            startPollingThread()
        }
    }

    private fun startPollingThread() {
        val poller = Thread {
            longPollingService.init()
            while (true) {
                longPollingService.poll()
            }
        }
        poller.isDaemon = true
        poller.name = "update-poller"
        poller.start()
    }
}