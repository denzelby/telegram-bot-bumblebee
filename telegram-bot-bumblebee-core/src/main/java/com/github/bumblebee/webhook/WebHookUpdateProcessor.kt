package com.github.bumblebee.webhook

import com.github.bumblebee.bot.consumer.TelegramUpdateRouter
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import java.util.concurrent.Executors

@Component
class WebHookUpdateProcessor(private val updateConsumer: TelegramUpdateRouter) {
    private val executor = Executors.newFixedThreadPool(4)

    fun process(update: Update) {
        executor.submit { updateConsumer.accept(update) }
    }
}
