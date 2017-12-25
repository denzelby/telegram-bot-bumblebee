package com.github.bumblebee.bot

import com.github.bumblebee.polling.LongPollingService
import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator
import com.github.bumblebee.webhook.registration.WebHookConfig
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class BumblebeeUpdateService(private val longPollingService: LongPollingService,
                             private val webHookConfig: WebHookConfig,
                             private val webHookRegistrator: TelegramWebHookRegistrator,
                             private val bumblebeeConfig: BumblebeeConfig) {

    @PostConstruct
    fun start() {
        if (webHookConfig.isEnabled && bumblebeeConfig.url != null) {
            webHookRegistrator.registerWebHook(bumblebeeConfig.url!!, bumblebeeConfig.certificatePath)
        } else {
            webHookRegistrator.removeWebHook()
            longPollingService.startPolling()
        }
    }
}
