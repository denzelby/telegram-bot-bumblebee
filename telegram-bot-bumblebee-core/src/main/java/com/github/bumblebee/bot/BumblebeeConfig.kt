package com.github.bumblebee.bot

import com.github.telegram.api.BotApi
import com.github.telegram.api.TelegramBot
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

data class WebhookConfig(var enabled: Boolean = false)

@Configuration
@ConfigurationProperties(prefix = "bumblebee")
class BumblebeeConfig {
    lateinit var token: String
    var url: String = ""
    var certificatePath: String? = null
    var commands: Map<String, String> = HashMap()
    var webhook: WebhookConfig = WebhookConfig()

    @Bean
    fun telegramApi(): BotApi = TelegramBot.create(token)
}
