package com.github.bumblebee.bot

import com.github.telegram.api.BotApi
import com.github.telegram.api.TelegramBot
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BumblebeeBot(val config: BumblebeeConfig) {

    @Bean
    open fun create(): BotApi = TelegramBot.create(config.token, logLevel = HttpLoggingInterceptor.Level.BODY)
}