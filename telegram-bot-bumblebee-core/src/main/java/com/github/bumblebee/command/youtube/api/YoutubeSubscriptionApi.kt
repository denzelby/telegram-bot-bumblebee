package com.github.bumblebee.command.youtube.api

import feign.*

interface YoutubeSubscriptionApi {

    @RequestLine("POST /")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @Body("hub.mode={hub.mode}&hub.topic={hub.topic}&hub.callback={hub.callback}")
    fun subscribe(@Param("hub.mode") hubMode: String,
                  @Param("hub.topic") hubTopicUrl: String,
                  @Param("hub.callback") hubCallbackUrl: String): Response

    companion object {
        val API_ROOT = "https://pubsubhubbub.appspot.com"
    }
}
