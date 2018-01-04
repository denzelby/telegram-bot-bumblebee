package com.github.bumblebee.command.youtube.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class SubscriptionUpdateSheduler(private val service: YoutubeSubscriptionService) {

    @Scheduled(fixedRate = delay)
    fun checkOverdueSubscriptions() {
        val date = Date()
        for (sub in service.existingSubscriptions) {
            val interval = date.time - sub.updatedDate.time
            if (interval > overdueInterval) {
                sub.updatedDate = date
                if (service.subscribeChannel(sub.channelId))
                    service.storeSubscription(sub)
            }
        }
    }

    companion object {
        private const val delay = (60 * 1000 * 60 * 4).toLong() //4 Hours in millis
        private val overdueInterval = TimeUnit.DAYS.toMillis(4)
    }

}
