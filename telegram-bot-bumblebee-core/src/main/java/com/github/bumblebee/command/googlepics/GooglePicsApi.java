package com.github.bumblebee.command.googlepics;

import feign.Param;
import feign.RequestLine;

public interface GooglePicsApi {

    @RequestLine("GET /search/images?v=1.0&q={query}")
    GooglePicsResponse queryPictures(@Param("query") String search);
}
