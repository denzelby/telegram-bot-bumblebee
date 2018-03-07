package com.github.bumblebee.bot

import com.github.bumblebee.polling.LongPollingService
import com.github.bumblebee.util.logger
import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.annotation.PostConstruct

@Configuration
class BumblebeeBot(private val config: BumblebeeConfig,
                   private val webHookRegistrator: TelegramWebHookRegistrator,
                   private val longPollingService: LongPollingService) {

    @PostConstruct
    fun start() {
        val timeZone = TimeZone.getTimeZone(config.timezone ?: "UTC")
        logger<BumblebeeBot>().info("Using timezone: $timeZone")
        TimeZone.setDefault(timeZone)

        if (config.webhook.enabled && config.url.isNotEmpty()) {
            webHookRegistrator.registerWebHook(config.url, config.certificatePath)
        } else {
            webHookRegistrator.removeWebHook()
            startPollingThread()
        }
    }

    private fun startPollingThread() {
        Thread {
            while (true) {
                longPollingService.poll()
            }
        }.apply {
            isDaemon = true
            name = "update-poller"
            start()
        }
    }
}