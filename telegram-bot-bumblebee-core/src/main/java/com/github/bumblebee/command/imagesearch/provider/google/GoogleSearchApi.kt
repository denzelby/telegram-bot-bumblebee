package com.github.bumblebee.command.imagesearch.provider.google

import com.github.bumblebee.command.imagesearch.domain.Image
import feign.Param
import feign.RequestLine

class GoogleSearchItem : Image {
    lateinit var link: String
    lateinit var mime: String

    override val url: String
        get() = link

    override val contentType: String
        get() = mime
}

data class GoogleCustomSearchResponse(var items: List<GoogleSearchItem>?)

interface GoogleSearchApi {

    @RequestLine("GET /v1?q={query}&searchType=image&imgSize=large&imgSize=xlarge&key={key}&cx={cx}&num=10&safe=medium")
    fun queryPictures(
        @Param("query") query: String,
        @Param("key") key: String,
        @Param("cx") customSearchId: String
    ): GoogleCustomSearchResponse

    companion object {
        const val API_ROOT = "https://www.googleapis.com/customsearch"
    }
}
