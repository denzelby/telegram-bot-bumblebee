package com.github.bumblebee.bot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "bumblebee")
class BumblebeeConfig {
    lateinit var token: String
    var url: String? = null
    var certificatePath: String? = null
    var commands: Map<String, String> = HashMap()
}
