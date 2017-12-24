package com.github.bumblebee.command.youtube

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
@ConfigurationProperties(prefix = "youtube.api")
class YoutubeSearchConfig {

    var projectName: String? = null
    var key: String? = null

    val isAvailable: Boolean
        get() = !StringUtils.isEmpty(projectName) && !StringUtils.isEmpty(key)
}
