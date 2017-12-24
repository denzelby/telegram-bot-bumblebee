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
    private val postedVideos: MutableList<PostedVideo> = service.retrievePostedVideos()

    fun process(feed: AtomFeed) {
        val videoId = feed.entry.videoId
        if (!postedVideos.any { it.videoId == videoId }) {
            postVideo(feed)
            markVideoAsPosted(videoId)
        }
    }

    private fun postVideo(feed: AtomFeed) {
        for (chatId in service.getChatIds(feed.entry.channelId)) {
            botApi.sendMessage(chatId, VIDEO_URL + feed.entry.videoId).execute()
        }
    }

    private fun markVideoAsPosted(videoId: String) {
        val postedVideo = PostedVideo()
        postedVideo.videoId = videoId
        postedVideo.postedDate = Date()
        postedVideos.add(postedVideo)
        service.addPostedVideo(postedVideo)
    }

    companion object {
        private val VIDEO_URL = "https://www.youtube.com/watch?v="
    }

}
