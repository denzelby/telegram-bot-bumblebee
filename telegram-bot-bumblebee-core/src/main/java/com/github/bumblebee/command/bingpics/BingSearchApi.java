package com.github.bumblebee.command.bingpics;

import feign.Param;
import feign.RequestLine;

public interface BingSearchApi {

    @RequestLine("GET /Image?$format=json&Query={query}&$top=10")
    BingSearchResponse queryPictures(@Param("query") String search);
}
