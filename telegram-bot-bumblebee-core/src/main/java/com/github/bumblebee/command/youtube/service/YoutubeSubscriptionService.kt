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

    private val existingSubscriptions: MutableList<Subscription> = subscriptionRepository.findAll().toMutableList()

    fun getSubscriptions(): MutableList<Subscription> = existingSubscriptions

    fun storeSubscription(subscription: Subscription) {
        subscriptionRepository.save(subscription)
    }

    fun deleteSubscription(subscription: Subscription) {
        subscriptionRepository.delete(subscription)
    }

    fun subscribeChannel(channelId: String): Boolean {
        val response = youtubeSubscriptionApi.subscribe(
                hubMode = "subscribe",
                hubTopicUrl = CHANNEL_URL + channelId,
                hubCallbackUrl = config.url + URL_POSTFIX
        )
        return response.status() == 202
    }

    fun unsubscribeChannel(channelId: String): Boolean {
        val response = youtubeSubscriptionApi.subscribe(
                hubMode = "unsubscribe",
                hubTopicUrl = CHANNEL_URL + channelId,
                hubCallbackUrl = config.url + URL_POSTFIX
        )
        return response.status() == 202
    }

    fun getChatIds(channelId: String): Set<Long> {
        return existingSubscriptions.asSequence()
                .filter { it.channelId == channelId }
                .flatMap { it.chats.asSequence() }
                .map { it.chatId }
                .toSet()
    }

    fun addPostedVideo(video: PostedVideo) {
        postedVideosRepository.save(video)
    }

    fun retrievePostedVideos(): Iterable<PostedVideo> {
        return postedVideosRepository.findAll()
    }

    companion object {
        private val CHANNEL_URL = "https://www.youtube.com/xml/feeds/videos.xml?channel_id="
        private val URL_POSTFIX = "/youtube"
    }
}
