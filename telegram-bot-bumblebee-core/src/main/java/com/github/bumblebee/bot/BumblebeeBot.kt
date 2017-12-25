package com.github.bumblebee.bot

import com.github.telegram.api.BotApi
import com.github.telegram.api.TelegramBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BumblebeeBot(val config: BumblebeeConfig) {

    @Bean
    fun telegramApi(): BotApi = TelegramBot.create(config.token)
}