package com.github.bumblebee.command.youtube.entity

import javax.persistence.*

@Entity
@Table(name = "BB_YOUTUBE_SUBBED_CHATS")
class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var chatId: Long = 0

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_SUBID")
    var subscription: Subscription? = null

    @Suppress("unused")
    constructor()

    constructor(chatId: Long, subscription: Subscription) {
        this.chatId = chatId
        this.subscription = subscription
    }

    override fun toString(): String = "chatId: $chatId"
}
