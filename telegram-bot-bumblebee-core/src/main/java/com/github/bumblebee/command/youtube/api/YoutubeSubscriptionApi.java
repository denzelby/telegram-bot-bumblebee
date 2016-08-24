package com.github.bumblebee.command.youtube.api;

import feign.*;

public interface YoutubeSubscriptionApi {

    String API_ROOT = "https://pubsubhubbub.appspot.com";

    @RequestLine("POST /")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @Body("hub.mode={hub.mode}&hub.topic={hub.topic}&hub.callback={hub.callback}")
    Response subscribe(@Param("hub.mode") String hubMode,
                       @Param("hub.topic") String hubTopicUrl,
                       @Param("hub.callback") String hubCallbackUrl);
}
