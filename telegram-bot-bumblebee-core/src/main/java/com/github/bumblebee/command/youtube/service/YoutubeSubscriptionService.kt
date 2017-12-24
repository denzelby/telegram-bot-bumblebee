package com.github.bumblebee.command.youtube.service

import com.github.bumblebee.bot.BumblebeeConfig
import com.github.bumblebee.command.youtube.api.YoutubeSubscriptionApi
import com.github.bumblebee.command.youtube.dao.YoutubePostedVideosRepository
import com.github.bumblebee.command.youtube.dao.YoutubeSubscriptionRepository
import com.github.bumblebee.command.youtube.entity.PostedVideo
import com.github.bumblebee.command.youtube.entity.Subscription
import feign.Feign
import feign.slf4j.Slf4jLogger
import org.springframework.stereotype.Service

@Service
class YoutubeSubscriptionService(private val subscriptionRepository: YoutubeSubscriptionRepository,
                                 private val postedVideosRepository: YoutubePostedVideosRepository,
                                 private val config: BumblebeeConfig) {

    private val youtubeSubscriptionApi: YoutubeSubscriptionApi = Feign.builder()
            .logLevel(feign.Logger.Level.BASIC)
            .logger(Slf4jLogger())
            .target(YoutubeSubscriptionApi::class.java, YoutubeSubscriptionApi.API_ROOT)

    val existingSubscriptions: MutableList<Subscription> = retrieveSubscriptions()

    fun storeSubscription(subscription: Subscription) {
        subscriptionRepository.save(subscription)
    }

    private fun retrieveSubscriptions(): MutableList<Subscription> {
        return subscriptionRepository.findAll().toMutableList()
    }

    fun deleteSubscription(subscription: Subscription) {
        subscriptionRepository.delete(subscription)
    }

    fun subscribeChannel(channelId: String): Boolean {
        val response = youtubeSubscriptionApi.subscribe("subscribe", CHANNEL_URL + channelId, config.url + URL_POSTFIX)
        return response.status() == 202
    }

    fun unsubscribeChannel(channelId: String): Boolean {
        val response = youtubeSubscriptionApi.subscribe("unsubscribe", CHANNEL_URL + channelId, config.url + URL_POSTFIX)
        return response.status() == 202
    }

    fun getChatIds(channelId: String): Set<Long> {
        return existingSubscriptions
                .filter { it.channelId == channelId }
                .flatMap { it.chats }
                .map { it.chatId }
                .toSet<Long>()
    }

    fun addPostedVideo(video: PostedVideo) {
        postedVideosRepository.save(video)
    }

    fun retrievePostedVideos(): MutableList<PostedVideo> {
        return postedVideosRepository.findAll().toMutableList()
    }

    companion object {
        private val CHANNEL_URL = "https://www.youtube.com/xml/feeds/videos.xml?channel_id="
        private val URL_POSTFIX = "/youtube"
    }

}
