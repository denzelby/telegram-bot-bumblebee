package com.github.bumblebee.command.imagesearch.provider.google

import com.github.bumblebee.command.imagesearch.domain.Image
import com.github.bumblebee.command.imagesearch.domain.ImageProvider
import com.github.bumblebee.util.logger
import feign.Feign
import feign.gson.GsonDecoder
import feign.slf4j.Slf4jLogger
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Component

@Component
class GoogleCustomSearchProvider(private val config: GoogleCustomSearchConfig) : ImageProvider {

    private val searchApi: GoogleSearchApi = Feign.builder()
            .decoder(GsonDecoder())
            .logLevel(feign.Logger.Level.BASIC)
            .logger(Slf4jLogger())
            .target(GoogleSearchApi::class.java, GoogleSearchApi.API_ROOT)

    override fun search(query: String): List<Image> {
        val response = searchApi.queryPictures(query, config.key, config.customSearchId)
        val items = response.items ?: emptyList()
        return if (items.isNotEmpty()) {
            log.info("Google found {} results for: {}", items.size, query)

            return items.filter { item -> !FilenameUtils.getExtension(item.link).isEmpty() }
        } else emptyList()
    }

    companion object {
        private val log = logger<GoogleCustomSearchProvider>()
    }
}
