package com.github.bumblebee.command.imagesearch.provider.bing

import com.github.bumblebee.command.imagesearch.domain.Image
import com.github.bumblebee.command.imagesearch.domain.ImageProvider
import com.github.bumblebee.util.logger
import feign.Feign
import feign.Logger
import feign.auth.BasicAuthRequestInterceptor
import feign.gson.GsonDecoder
import feign.slf4j.Slf4jLogger
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Component

@Component
class BingPictureSearchProvider(config: BingSearchConfig) : ImageProvider {

    private val imageSearchApi: BingSearchApi = Feign.builder()
            .decoder(GsonDecoder())
            .logLevel(Logger.Level.BASIC)
            .logger(Slf4jLogger())
            .requestInterceptor(BasicAuthRequestInterceptor("", config.accountKey))
            .target(BingSearchApi::class.java, BingSearchApi.API_ROOT)

    private val ignoredTypes = hashSetOf("image/animatedgif")

    override fun search(query: String): List<Image> {
        val response = imageSearchApi.queryPictures("'$query'")
        response.data?.results?.let { items ->
            return items.filter { pic ->
                !ignoredTypes.contains(pic.contentType) && FilenameUtils.getExtension(pic.mediaUrl).isNotEmpty()
            }
        }

        log.error("Bad Bing response: {}", response.data)
        return emptyList()
    }

    companion object {
        private val log = logger<BingPictureSearchProvider>()
    }
}
