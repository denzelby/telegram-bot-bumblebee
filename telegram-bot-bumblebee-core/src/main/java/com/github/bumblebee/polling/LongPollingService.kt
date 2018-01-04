package com.github.bumblebee.polling

import com.github.bumblebee.bot.consumer.UpdateProcessor
import com.github.bumblebee.util.loggerFor
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Service

@Service
class LongPollingService(private val botApi: BotApi,
                         private val updateProcessor: UpdateProcessor) {

    private var lastUpdateOffset: Long = 0

    fun poll() {
        try {
            log.debug("Calling getUpdates() with offset {}", lastUpdateOffset)
            val response = botApi.getUpdates(lastUpdateOffset, pollItemsBatchSize, pollTimeoutSec)

            if (response.ok) {
                val updates = response.result.orEmpty()
                updates.forEach { updateProcessor.process(it) }

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

    private fun updateLastUpdateOffset(updates: List<Update>) {
        if (updates.isNotEmpty()) {
            lastUpdateOffset = updates.last().updateId + 1
        }
    }

    companion object {
        private val log = loggerFor<LongPollingService>()

        private val pollTimeoutSec = 60
        private val pollItemsBatchSize = 100
    }
}
