package com.github.bumblebee.command.imagesearch

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.imagesearch.domain.Image
import com.github.bumblebee.command.imagesearch.domain.ImageProvider
import com.github.bumblebee.service.RandomPhraseService
import com.github.bumblebee.util.loggerFor
import com.github.telegram.api.BotApi
import com.github.telegram.domain.ChatAction
import com.github.telegram.domain.Update
import java.util.concurrent.CompletableFuture

open class ImageSearchCommand(private val botApi: BotApi,
                              private val randomPhrase: RandomPhraseService,
                              private val providers: List<ImageProvider>,
                              private val preprocessor: (List<Image>) -> List<Image>) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhrase.surprise())
            return
        }

        CompletableFuture.runAsync {
            botApi.sendChatAction(chatId, ChatAction.UPLOAD_PHOTO)
        }

        providers.forEach { provider ->
            val images = search(provider, argument)
            if (images.isNotEmpty()) {
                val isSent = sendFirstImage(images, chatId, argument)
                if (isSent) {
                    return
                }
            }
            log.warn("Provider {} failed to find images", provider.name())
        }
        botApi.sendMessage(chatId, randomPhrase.no(), update.message!!.messageId)
    }

    private fun search(provider: ImageProvider, query: String): List<Image> {
        return try {
            provider.search(query)
        } catch (e: Exception) {
            log.error("Search failed", e)
            emptyList()
        }
    }

    private fun sendFirstImage(pictures: List<Image>, chatId: Long, caption: String): Boolean {
        preprocessor(pictures).forEach { picture ->
            try {
                if (!ignoredContentTypes.contains(picture.contentType)) {
                    log.info("Sending ({}): {}", picture.contentType, picture.url)
                    botApi.sendPhoto(chatId, picture.url, caption)
                    return true
                }
            } catch (e: Exception) {
                log.error("Image send failed, retrying. Url={}, cause={}", picture.url, e.message)
            }
        }
        return false
    }

    companion object {
        private val log = loggerFor<ImageSearchCommand>()
        private val ignoredContentTypes = setOf("image/svg+xml", "image/animatedgif")
    }
}
