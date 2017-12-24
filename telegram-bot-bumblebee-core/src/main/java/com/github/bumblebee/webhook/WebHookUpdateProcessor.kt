package com.github.bumblebee.webhook

import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import telegram.polling.HandlerRegistry
import telegram.polling.TelegramUpdateConsumer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Component
class WebHookUpdateProcessor(handlerRegistry: HandlerRegistry) {

    private val updateConsumer: TelegramUpdateConsumer = TelegramUpdateConsumer(handlerRegistry)
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    fun process(update: Update) {
        executor.submit { updateConsumer.accept(update) }
    }
}
