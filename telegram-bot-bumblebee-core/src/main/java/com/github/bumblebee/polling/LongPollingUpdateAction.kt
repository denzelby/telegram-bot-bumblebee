package com.github.bumblebee.polling

import com.github.telegram.api.BotApi
import com.github.telegram.api.Response
import com.github.telegram.domain.Update
import org.slf4j.LoggerFactory

internal class LongPollingUpdateAction(private val botApi: BotApi,
                                       private val updateConsumer: (Update) -> Unit) : Runnable {
    private var lastUpdateOffset: Long = 0

    override fun run() {
        try {
            log.info("getUpdates with offset {}", lastUpdateOffset)
            val response = botApi.getUpdates(lastUpdateOffset, POLL_ITEMS_BATCH_SIZE, POLL_TIMEOUT_SEC)
            log.info("Got {} updates", response.result?.size)
            processUpdates(response)
        } catch (e: RuntimeException) {
            log.error("Failed to call telegram api", e)
        }
    }

    private fun processUpdates(response: Response<List<Update>>) {
        if (!response.ok || response.result == null) {
            log.error("Update failed, offset = {}", lastUpdateOffset)
            updateLastUpdateOffset(response.result)
            return
        }

        val updates = response.result!!

        updates.forEach(updateConsumer)

        if (updates.isNotEmpty()) {
            updateLastUpdateOffset(updates)
        }
    }

    private fun updateLastUpdateOffset(updates: List<Update>?) {
        if (updates != null && updates.isNotEmpty()) {
            val (updateId) = updates[updates.size - 1]
            log.trace("offset: {} -> {}", lastUpdateOffset, updateId + 1)
            lastUpdateOffset = updateId + 1
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(LongPollingUpdateAction::class.java)

        private val POLL_TIMEOUT_SEC = 60
        private val POLL_ITEMS_BATCH_SIZE = 100
    }
}
