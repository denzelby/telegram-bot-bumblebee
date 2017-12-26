package com.github.bumblebee.webhook.registration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "bumblebee.webhook")
class WebHookConfig {
    var isEnabled: Boolean = false
}
