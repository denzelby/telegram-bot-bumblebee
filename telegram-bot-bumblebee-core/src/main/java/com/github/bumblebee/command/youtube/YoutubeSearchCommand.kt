package com.github.bumblebee.command.youtube

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.service.RandomPhraseService
import com.github.bumblebee.util.loggerFor
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class YoutubeSearchCommand(private val botApi: BotApi,
                           private val randomPhraseService: RandomPhraseService,
                           config: YoutubeSearchConfig) : SingleArgumentCommand() {
    private val youtube: YouTube
    private val googleApiKey: String

    init {
        if (!config.isAvailable) {
            throw IllegalStateException("Youtube api configuration missing")
        }
        this.googleApiKey = config.key!!

        this.youtube = YouTube.Builder(ApacheHttpTransport(), JacksonFactory()) { }
                .setApplicationName(config.projectName)
                .build()
    }

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhraseService.surprise())
            return
        }

        var retries = RETRY_COUNT
        while (retries-- > 0) {
            try {
                val videoId = searchVideo(argument)
                if (videoId != null) {
                    botApi.sendMessage(chatId, VIDEO_URL + videoId)
                    return
                } else {
                    log.info("Video search failed, retrying... (attempt {})", RETRY_COUNT - retries)
                }
            } catch (e: IOException) {
                log.error("Error during youtube search", e)
            }
        }

        val message = "${randomPhraseService.no()}. No, really, I've tried $RETRY_COUNT times."
        botApi.sendMessage(chatId, message, update.message!!.messageId)
    }

    @Throws(IOException::class)
    private fun searchVideo(searchQuery: String): String? {

        // Define the API request for retrieving search results.
        val search = youtube.search().list("id")

        search.key = googleApiKey
        search.q = searchQuery

        // Restrict the search results to only include videos.
        search.type = "video"

        // To increase efficiency, only retrieve the fields that the application uses.
        search.fields = "items(id/videoId)"
        search.maxResults = NUMBER_OF_VIDEOS_RETURNED

        // Call the API and print results.
        val searchResponse = search.execute()
        val searchResultList = searchResponse.items
        return if (searchResultList != null && !searchResultList.isEmpty()) {
            searchResultList[0].id.videoId
        } else null
    }

    companion object {

        private val log = loggerFor<YoutubeSearchCommand>()

        private val VIDEO_URL = "https://www.youtube.com/watch?v="
        private val NUMBER_OF_VIDEOS_RETURNED = 1L
        private val RETRY_COUNT = 5
    }

}
