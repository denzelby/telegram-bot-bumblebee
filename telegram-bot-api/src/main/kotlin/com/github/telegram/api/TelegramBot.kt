package com.github.telegram.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import feign.Feign
import feign.Logger
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.slf4j.Slf4jLogger

object TelegramBot {

    fun create(token: String): BotApi {

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()!!

        return Feign.builder()
                .decoder(GsonDecoder(gson))
                .encoder(MultipartEncoder(GsonEncoder(gson)))
                .logger(Slf4jLogger())
                .logLevel(Logger.Level.BASIC)
                .target(BotApi::class.java, "https://api.telegram.org/bot$token/")
    }
}