package com.github.bumblebee.command.youtube

import com.github.bumblebee.command.youtube.entity.PostedVideo
import com.github.bumblebee.command.youtube.entity.VideoNotification
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService
import com.github.bumblebee.util.logger
import com.github.telegram.api.BotApi
import org.springframework.stereotype.Component
import java.util.*

@Component
class YoutubeUpdateProcessor(private val botApi: BotApi,
                             private val service: YoutubeSubscriptionService) {
    private val postedVideos: MutableList<PostedVideo> = service.retrievePostedVideos().toMutableList()

    fun process(videoNotification: VideoNotification) {
        if (!postedVideos.any { it.videoId == videoNotification.videoId }) {
            postVideo(videoNotification)
            markVideoAsPosted(videoNotification.videoId)
        }
    }

    private fun postVideo(videoNotification: VideoNotification) {
        log.info("Posting video: {}", videoNotification)
        service.getChatIds(videoNotification.channelId).forEach { chatId ->
            val url = VIDEO_URL + videoNotification.videoId
            log.info("Posting video to chat {}: {}", chatId, url)
            botApi.sendMessage(chatId, url)
        }
    }

    private fun markVideoAsPosted(videoId: String) {
        val video = PostedVideo(videoId, Date())
        postedVideos.add(video)
        service.addPostedVideo(video)
    }

    companion object {
        val log = logger<YoutubeUpdateProcessor>()
        private val VIDEO_URL = "https://www.youtube.com/watch?v="
    }
}
