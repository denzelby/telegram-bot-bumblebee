package com.github.bumblebee.command.youtube.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "BB_YOUTUBE_POSTED_VIDEOS")
class PostedVideo {

    @Id
    var videoId: String? = null
    var postedDate: Date? = null

    @Suppress("unused")
    constructor()

    constructor(videoId: String, postedDate: Date) {
        this.videoId = videoId
        this.postedDate = postedDate
    }

    override fun toString(): String = "PostedVideo(videoId=$videoId, postedDate=$postedDate)"
}
