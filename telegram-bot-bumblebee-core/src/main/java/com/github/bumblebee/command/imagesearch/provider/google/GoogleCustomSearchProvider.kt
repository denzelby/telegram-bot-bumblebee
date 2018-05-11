package com.github.bumblebee.command.imagesearch.provider.google

import com.github.bumblebee.command.imagesearch.domain.Image
import com.github.bumblebee.command.imagesearch.domain.ImageProvider
import feign.Feign
import feign.gson.GsonDecoder
import feign.slf4j.Slf4jLogger
import org.springframework.stereotype.Component

@Component
class GoogleCustomSearchProvider(private val config: GoogleCustomSearchConfig) : ImageProvider {

    private val searchApi: GoogleSearchApi = Feign.builder()
        .decoder(GsonDecoder())
        .logLevel(feign.Logger.Level.BASIC)
        .logger(Slf4jLogger())
        .target(GoogleSearchApi::class.java, GoogleSearchApi.API_ROOT)

    override fun search(query: String): List<Image> =
        searchApi.queryPictures(query, config.key, config.customSearchId).items.orEmpty()

    override fun name(): String = "Google"
}
