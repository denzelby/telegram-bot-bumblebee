package com.github.bumblebee.command.imagesearch.provider.google

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "google.api")
class GoogleCustomSearchConfig {
    lateinit var key: String
    lateinit var customSearchId: String
}
