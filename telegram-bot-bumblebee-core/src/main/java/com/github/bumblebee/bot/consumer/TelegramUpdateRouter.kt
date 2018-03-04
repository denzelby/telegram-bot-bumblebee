package com.github.bumblebee.bot.consumer

import com.github.bumblebee.util.logger
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.regex.Pattern

/**
 * Route update to concrete handler using handler registry
 */
@Component
class TelegramUpdateRouter(private val handlerRegistry: HandlerRegistry) {

    fun route(update: Update) {
        try {
            if (isOutdatedUpdate(update)) {
                log.debug("Outdated update skipped")
                return
            }
            // try invoking command
            val consumed = invokeCommand(update)

            if (!consumed) {
                // run handler chain if not consumed, stop if handler returns true
                handlerRegistry.handlerChain.any { it.onUpdate(update) }
            }
        } catch (e: Exception) {
            log.error("Exception during update processing", e)
        }
    }

    private fun isOutdatedUpdate(update: Update): Boolean {
        val unixTime = update.message?.date
        return unixTime != null && Instant.ofEpochSecond(unixTime.toLong())
                .isBefore(Instant.now().minusSeconds(updateExpirationSeconds.toLong()))
    }

    private fun invokeCommand(update: Update): Boolean {
        val text = update.message?.text
        if (text != null && text.startsWith("/")) {
            val handler = handlerRegistry[parse(text)]
            if (handler != null) {
                handler.onUpdate(update)
                return true
            }
        }
        return false
    }

    private fun parse(message: String): String? {
        val matcher = commandPattern.matcher(message)
        return if (matcher.find()) {
            matcher.group()
        } else null
    }

    companion object {
        private val log = logger<TelegramUpdateRouter>()

        private val updateExpirationSeconds = 2 * 60
        private val commandPattern = Pattern.compile("^/[\\w]+")
    }
}
