package com.github.bumblebee.command.imagesearch.provider.google;

import com.github.bumblebee.command.imagesearch.provider.google.response.GoogleCustomSearchResponse;
import feign.Param;
import feign.RequestLine;

public interface GoogleSearchApi {

    String API_ROOT = "https://www.googleapis.com/customsearch";

    @RequestLine("GET /v1?q={query}&searchType=image&imgSize=large&imgSize=xlarge&key={key}&cx={cx}&num=10&safe=medium")
    GoogleCustomSearchResponse queryPictures(@Param("query") String query, @Param("key") String key, @Param("cx") String customSearchId);
}
