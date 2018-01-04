package com.github.bumblebee.polling

import com.github.bumblebee.bot.consumer.TelegramUpdateRouter
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class LongPollingService(private val botApi: BotApi,
                         private val updateConsumer: TelegramUpdateRouter) {

    private lateinit var executors: List<ExecutorService>
    private var lastUpdateOffset: Long = 0

    fun init() {
        log.info("Initializing long polling service")
        executors = (0..executorsCount).map { Executors.newSingleThreadExecutor() }
    }

    fun poll() {
        try {
            log.debug("Calling getUpdates() with offset {}", lastUpdateOffset)
            val response = botApi.getUpdates(lastUpdateOffset, pollItemsBatchSize, pollTimeoutSec)

            if (response.ok) {
                val updates = response.result.orEmpty()
                processUpdates(updates)

                if (updates.isNotEmpty()) {
                    updateLastUpdateOffset(updates)
                }
            } else {
                log.error("Update failed, offset = {}", lastUpdateOffset)
            }
        } catch (e: RuntimeException) {
            log.error("Failed to call telegram api", e)
        }
    }

    private fun processUpdates(updates: List<Update>) {
        updates.forEach { update ->
            // updates from the same chats goes to the same executor
            val executorIndex = update.senderId.hashCode() % executors.size
            log.debug("Selected executor: {}", executorIndex)
            executors[executorIndex].submit {
                updateConsumer.accept(update)
            }
        }
    }

    private fun updateLastUpdateOffset(updates: List<Update>) {
        if (updates.isNotEmpty()) {
            lastUpdateOffset = updates.last().updateId + 1
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(LongPollingService::class.java)

        private val executorsCount = 4
        private val pollTimeoutSec = 60
        private val pollItemsBatchSize = 100
    }
}
