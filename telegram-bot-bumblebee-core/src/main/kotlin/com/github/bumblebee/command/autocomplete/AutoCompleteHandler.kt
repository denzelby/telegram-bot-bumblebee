package com.github.bumblebee.command.autocomplete

import com.github.bumblebee.command.ChainedMessageListener
import com.github.bumblebee.command.autocomplete.entity.AutoCompletePhrase
import com.github.bumblebee.command.autocomplete.service.AutoCompleteService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap


@Component
class AutoCompleteHandler(
    private val botApi: BotApi,
    private val service: AutoCompleteService
) : ChainedMessageListener() {

    private val completions: MutableMap<String, List<String>> = service.fetchCompletions().associateByTo(
        destination = ConcurrentHashMap(),
        keySelector = { it.phraseKey },
        valueTransform = { splitPhrases(it.phrasePattern) }
    )

    fun addCompletion(template: String): Boolean {
        if (!isValidTemplate(template)) {
            return false
        }

        val parts = splitPhrases(template, limit = 2)
        val patternKey = parts.first()
        val patternPhrase = parts.last()

        if (completions.containsKey(patternKey))
            service.updateAutoCompletePhrase(patternKey, patternPhrase)
        else
            service.storeAutoCompletePhrase(AutoCompletePhrase(patternKey, patternPhrase))
        completions[patternKey] = splitPhrases(patternPhrase)
        return true
    }

    private fun isValidTemplate(argument: String): Boolean = templateRegex.matches(argument)

    private fun splitPhrases(pattern: String, limit: Int = 0) = pattern.split('/', limit = limit)

    override fun onMessage(chatId: Long, message: String?, update: Update): Boolean {
        message?.let {
            completions[it]?.let { phrases ->
                phrases.forEach { phrase ->
                    if (phrase.startsWith(STICKER_PREFIX)) {
                        botApi.sendSticker(chatId, phrase.removePrefix(STICKER_PREFIX))
                    } else {
                        botApi.sendMessage(chatId, phrase)
                    }
                    Thread.sleep(400)
                }
                return true
            }
        }
        return false
    }

    fun availableCompletions(): Set<String> = completions.keys

    companion object {
        private const val STICKER_PREFIX = "stickerId:"
        private val templateRegex = """[^/\n]+/([^/\n]+/)*[^/\n]+""".toRegex()
    }
}


