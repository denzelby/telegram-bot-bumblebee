package com.github.bumblebee.command.youtube

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "youtube.api")
class YoutubeSearchConfig {

    var projectName: String? = null
    var key: String? = null

    val isAvailable: Boolean
        get() = !projectName.isNullOrEmpty() && !key.isNullOrEmpty()
}
