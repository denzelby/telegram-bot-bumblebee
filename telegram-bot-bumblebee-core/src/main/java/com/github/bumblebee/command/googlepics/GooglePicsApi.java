package com.github.bumblebee.command.googlepics;

import feign.Param;
import feign.RequestLine;

public interface GooglePicsApi {

    // https://developers.google.com/image-search/v1/jsondevguide
    @RequestLine("GET /search/images?v=1.0&imgsz=medium|large&rsz=8&safe=active&q={query}")
    GooglePicsResponse queryPictures(@Param("query") String search);
}
