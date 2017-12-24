package com.github.telegram.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object TelegramBot {

    fun create(token: String, timeout: Int = 30, logLevel: Level = Level.BASIC): BotApi {

        val logging = HttpLoggingInterceptor().apply {
            level = logLevel
        }

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(timeout + 10L, TimeUnit.SECONDS)
                .readTimeout(timeout + 10L, TimeUnit.SECONDS)
                .writeTimeout(timeout + 10L, TimeUnit.SECONDS)
                .build()

        val gson = Gson()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.telegram.org/bot$token/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .validateEagerly(true)
                .build()

        return BotApi(retrofit.create(BotCoreApi::class.java))
    }
}