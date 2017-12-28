package com.github.bumblebee.bot.consumer

import com.github.telegram.domain.Update
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.function.Consumer
import java.util.regex.Pattern

@Component
class TelegramUpdateRouter(private val handlerRegistry: HandlerRegistry) : Consumer<Update> {

    override fun accept(update: Update) {
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

    private fun parse(message: String?): String? {
        val matcher = commandPattern.matcher(message)
        return if (matcher.find()) {
            matcher.group()
        } else null
    }

    companion object {
        private val log = LoggerFactory.getLogger(TelegramUpdateRouter::class.java)

        private val updateExpirationSeconds = 2 * 60
        private val commandPattern = Pattern.compile("^/[\\w]+")
    }
}
