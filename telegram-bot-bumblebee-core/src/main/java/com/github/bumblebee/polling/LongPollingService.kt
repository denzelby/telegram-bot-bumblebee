package com.github.bumblebee.polling

import com.github.telegram.api.BotApi
import org.springframework.stereotype.Service
import telegram.polling.HandlerRegistry
import telegram.polling.TelegramUpdateConsumer
import telegram.polling.TelegramUpdateService

@Service
class LongPollingService(botApi: BotApi, handlerRegistry: HandlerRegistry) {

    private val updateService: TelegramUpdateService =
            TelegramUpdateService(botApi, TelegramUpdateConsumer(handlerRegistry))

    fun startPolling() {
        updateService.startPolling()
    }
}
