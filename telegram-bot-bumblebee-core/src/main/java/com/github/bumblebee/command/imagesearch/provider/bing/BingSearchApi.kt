package com.github.bumblebee.command.imagesearch.provider.bing

import com.github.bumblebee.command.imagesearch.provider.bing.response.BingSearchResponse
import feign.Param
import feign.RequestLine

@Deprecated("Seems to be replaced with https://docs.microsoft.com/en-us/azure/cognitive-services/bing-web-search/overview")
interface BingSearchApi {

    @RequestLine("GET /Image?\$format=json&Query={query}&\$top=20&ImageFilters='Size:Medium'")
    fun queryPictures(@Param("query") search: String): BingSearchResponse

    companion object {
        val API_ROOT = "https://api.datamarket.azure.com/Bing/Search/v1"
    }
}
