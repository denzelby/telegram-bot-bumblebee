package com.github.bumblebee.bot

import com.github.bumblebee.polling.LongPollingService
import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator
import com.github.bumblebee.webhook.registration.WebHookConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class BumblebeeUpdateService(private val longPollingService: LongPollingService,
                             private val webHookConfig: WebHookConfig,
                             private val webHookRegistrator: TelegramWebHookRegistrator,
                             private val bumblebeeConfig: BumblebeeConfig) {

    @PostConstruct
    fun start() {
        if (webHookConfig.isEnabled) {
            webHookRegistrator.registerWebHook(bumblebeeConfig.url, bumblebeeConfig.certificatePath)
        } else {
            webHookRegistrator.removeWebHook()
            longPollingService.startPolling()
            log.info("Started polling service")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(BumblebeeUpdateService::class.java)
    }
}
