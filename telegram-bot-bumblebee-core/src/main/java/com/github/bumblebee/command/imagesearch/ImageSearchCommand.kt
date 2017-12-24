package com.github.bumblebee.command.imagesearch

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.imagesearch.domain.Image
import com.github.bumblebee.command.imagesearch.domain.ImageProvider
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import com.github.telegram.api.async
import com.github.telegram.domain.ChatAction
import com.github.telegram.domain.Update
import org.slf4j.LoggerFactory

open class ImageSearchCommand(private val botApi: BotApi,
                              private val randomPhrase: RandomPhraseService,
                              private val providers: List<ImageProvider>,
                              private val preprocessor: (List<Image>) -> Unit) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhrase.surprise()).execute()
            return
        }

        val messageId = update.message!!.messageId

        botApi.sendChatAction(chatId, ChatAction.UPLOAD_PHOTO).async()

        providers.forEach { provider ->
            val images = search(provider, argument)
            if (images.isNotEmpty()) {
                val isSent = sendRandomPicture(images, chatId, argument)
                if (isSent) {
                    return
                }
            }
            Companion.log.warn("Provider {} failed to find images", provider.name())
        }
        botApi.sendMessage(chatId, randomPhrase.no(), messageId).execute()
    }

    private fun search(provider: ImageProvider, query: String): List<Image> {
        return try {
            provider.search(query)
        } catch (e: Exception) {
            Companion.log.error("Search failed", e)
            emptyList()
        }
    }

    private fun sendRandomPicture(pictures: List<Image>, chatId: Long, caption: String): Boolean {

        preprocessor(pictures)

        for (picture in pictures) {
            try {
                Companion.log.info("Sending ({}): {}", picture.contentType, picture.url)
                botApi.sendPhoto(chatId, picture.url, caption).execute()
                return true
            } catch (e: Exception) {
                Companion.log.error("Image send failed, retrying. Url={}, cause={}", picture.url, e.message)
            }
        }
        return false
    }

    companion object {
        private val log = LoggerFactory.getLogger(ImageSearchCommand::class.java)
    }
}
