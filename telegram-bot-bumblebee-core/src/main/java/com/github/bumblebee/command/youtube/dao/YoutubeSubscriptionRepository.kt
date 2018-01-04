package com.github.bumblebee.command.youtube.dao

import com.github.bumblebee.command.youtube.entity.Subscription
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface YoutubeSubscriptionRepository : CrudRepository<Subscription, Long>
