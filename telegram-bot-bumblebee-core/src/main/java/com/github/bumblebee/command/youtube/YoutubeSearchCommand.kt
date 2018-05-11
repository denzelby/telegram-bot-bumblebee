package com.github.bumblebee.command.youtube

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.service.RandomPhraseService
import com.github.bumblebee.util.logger
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

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

        val message = searchVideo(argument)
                .orElse("${randomPhraseService.no()}. No, really, I've tried $RETRY_COUNT times.")
        botApi.sendMessage(chatId, message)
    }

    tailrec fun searchVideo(searchQuery: String, retries: Int = RETRY_COUNT): Optional<String> {
        if (retries == 0) {
            return Optional.empty()
        }
        try {
            queryFirstVideoId(searchQuery)?.let { videoId ->
                return Optional.of(VIDEO_URL + videoId)
            }
            log.warn("Video search failed, retrying... (remaining attempts {})", retries)
        } catch (e: IOException) {
            log.error("Error during youtube search", e)
        }
        return searchVideo(searchQuery, retries - 1)
    }

    @Throws(IOException::class)
    private fun queryFirstVideoId(searchQuery: String): String? {
        // Define the API request for retrieving search results.
        val response = with(youtube.search().list("id")) {
            key = googleApiKey
            q = searchQuery
            // Restrict the search results to only include videos.
            type = "video"
            // To increase efficiency, only retrieve the fields that the application uses.
            fields = "items(id/videoId)"
            maxResults = 1

            execute()
        }
        return response.items.orEmpty().firstOrNull()?.id?.videoId
    }

    companion object {
        private val log = logger<YoutubeSearchCommand>()

        private const val VIDEO_URL = "https://www.youtube.com/watch?v="
        private const val RETRY_COUNT = 3
    }
}
