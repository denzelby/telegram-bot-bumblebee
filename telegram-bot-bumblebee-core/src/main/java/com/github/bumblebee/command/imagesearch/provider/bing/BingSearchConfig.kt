package com.github.bumblebee.command.imagesearch.provider.bing

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "bing.api")
class BingSearchConfig{
    lateinit var accountKey: String
}
