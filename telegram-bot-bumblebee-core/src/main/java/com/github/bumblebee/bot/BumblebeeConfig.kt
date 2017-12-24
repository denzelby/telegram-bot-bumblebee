package com.github.bumblebee.bot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "bumblebee")
open class BumblebeeConfig {
    lateinit var token: String
    lateinit var url: String
    lateinit var certificatePath: String
    var commands: Map<String, String> = HashMap()
}
