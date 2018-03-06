package com.github.bumblebee.command.statistics.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "BB_STATISTICS")
class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var postedDate: LocalDate? = null

    var messageCount: Int = 0

    var chatId: Long = 0

    var authorId: Long = 0

    var authorName: String? = null

    @Suppress("unused")
    constructor()

    constructor(postedDate: LocalDate,
                messageCount: Int,
                chatId: Long,
                authorId: Long,
                authorName: String?) {
        this.postedDate = postedDate
        this.messageCount = messageCount
        this.chatId = chatId
        this.authorId = authorId
        this.authorName = authorName
    }
}