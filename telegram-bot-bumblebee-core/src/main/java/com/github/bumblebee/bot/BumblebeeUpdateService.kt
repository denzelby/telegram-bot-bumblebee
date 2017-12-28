package com.github.bumblebee.bot

import com.github.bumblebee.polling.LongPollingService
import com.github.bumblebee.webhook.registration.TelegramWebHookRegistrator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

// TODO: move body of this class to BumblebeeBot
@Service
class BumblebeeUpdateService(private val longPollingService: LongPollingService,
                             @Value("\${bumblebee.webhook.enabled}") private val webHookEnabled: Boolean,
                             private val webHookRegistrator: TelegramWebHookRegistrator,
                             private val bumblebeeConfig: BumblebeeConfig) {

    @PostConstruct
    fun start() {
        if (webHookEnabled && bumblebeeConfig.url != null) {
            webHookRegistrator.registerWebHook(bumblebeeConfig.url!!, bumblebeeConfig.certificatePath)
        } else {
            webHookRegistrator.removeWebHook()
            longPollingService.startPolling()
        }
    }
}
