package com.github.bumblebee.command.youtube

import com.github.bumblebee.command.youtube.entity.AtomFeed
import com.github.bumblebee.command.youtube.entity.PostedVideo
import com.github.bumblebee.command.youtube.service.YoutubeSubscriptionService
import com.github.telegram.api.BotApi
import org.springframework.stereotype.Component
import java.util.*

@Component
class YoutubeUpdateProcessor(private val botApi: BotApi,
                             private val service: YoutubeSubscriptionService) {
    private val postedVideos: MutableList<PostedVideo> = service.retrievePostedVideos().toMutableList()

    fun process(feed: AtomFeed) {
        val videoId = feed.entry.videoId
        if (!postedVideos.any { it.videoId == videoId }) {
            postVideo(feed)
            markVideoAsPosted(videoId)
        }
    }

    private fun postVideo(feed: AtomFeed) {
        service.getChatIds(feed.entry.channelId).forEach { chatId ->
            botApi.sendMessage(chatId, VIDEO_URL + feed.entry.videoId)
        }
    }

    private fun markVideoAsPosted(videoId: String) {
        val video = PostedVideo(videoId, Date())
        postedVideos.add(video)
        service.addPostedVideo(video)
    }

    companion object {
        private val VIDEO_URL = "https://www.youtube.com/watch?v="
    }
}
