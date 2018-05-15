package com.github.bumblebee.command.youtube.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class SubscriptionUpdateScheduler(private val service: YoutubeSubscriptionService) {

    @Scheduled(fixedRate = delay)
    fun checkOverdueSubscriptions() {
        val date = Date()
        service.getSubscriptions().forEach { subscription ->
            val interval = date.time - subscription.updatedDate.time
            if (interval > overdueInterval) {
                subscription.updatedDate = date
                if (service.subscribeChannel(subscription.channelId))
                    service.storeSubscription(subscription)
            }
        }
    }

    companion object {
        private const val delay = (60 * 1000 * 60 * 4).toLong() //4 Hours in millis
        private val overdueInterval = TimeUnit.DAYS.toMillis(4)
    }
}
