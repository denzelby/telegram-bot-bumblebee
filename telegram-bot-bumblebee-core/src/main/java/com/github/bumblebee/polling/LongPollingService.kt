package com.github.bumblebee.polling

import com.github.bumblebee.bot.consumer.TelegramUpdateRouter
import com.github.bumblebee.util.loggerFor
import com.github.telegram.api.BotApi
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

// TODO: rework
@Service
class LongPollingService(botApi: BotApi, updateConsumer: TelegramUpdateRouter) {

    private var executor: ScheduledExecutorService? = null
    private val updateAction = LongPollingUpdateAction(botApi, updateConsumer::accept)

    @Synchronized
    fun startPolling() {

        if (executor == null) {
            executor = Executors.newSingleThreadScheduledExecutor()
        }

        executor!!.scheduleWithFixedDelay(updateAction, 0, 1, TimeUnit.MILLISECONDS)

        log.debug("Polling started")
    }

    @Synchronized
    fun stopPolling() {
        if (executor != null) {
            executor!!.shutdown()
        }

        log.debug("Polling stopped")
    }

    companion object {
        private val log = loggerFor<LongPollingService>()
    }
}
