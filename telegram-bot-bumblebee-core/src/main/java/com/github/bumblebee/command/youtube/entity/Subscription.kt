package com.github.bumblebee.command.youtube.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "BB_YOUTUBE_SUBSCRIPTIONS")
class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var subId: Long? = null

    lateinit var channelId: String

    lateinit var updatedDate: Date

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = [(CascadeType.ALL)],
        orphanRemoval = true,
        mappedBy = "subscription"
    )
    var chats: MutableList<Chat> = ArrayList()

    override fun toString(): String =
        "Subscription(subId=$subId, channelId=$channelId, updatedDate=$updatedDate, chats=$chats)"
}
