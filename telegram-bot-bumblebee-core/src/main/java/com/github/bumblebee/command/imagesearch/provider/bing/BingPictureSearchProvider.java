package com.github.bumblebee.command.imagesearch.provider.bing;

import com.github.bumblebee.command.imagesearch.domain.Image;
import com.github.bumblebee.command.imagesearch.domain.ImageProvider;
import com.github.bumblebee.command.imagesearch.provider.bing.response.BingSearchData;
import com.github.bumblebee.command.imagesearch.provider.bing.response.BingSearchResponse;
import com.github.bumblebee.command.imagesearch.provider.bing.response.BingSearchResultItem;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BingPictureSearchProvider implements ImageProvider {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BingPictureSearchProvider.class);

    private final BingSearchApi imageSearchApi;
    private final Set<String> ignoredTypes = new HashSet<>();

    @Autowired
    public BingPictureSearchProvider(BingSearchConfig config) {

        this.imageSearchApi = Feign.builder()
                .decoder(new GsonDecoder())
                .logLevel(Logger.Level.BASIC)
                .logger(new Slf4jLogger())
                .requestInterceptor(new BasicAuthRequestInterceptor("", config.getAccountKey()))
                .target(BingSearchApi.class, BingSearchApi.API_ROOT);

        this.ignoredTypes.add("image/animatedgif");
    }

    @Override
    public List<Image> search(String query) {

        BingSearchResponse response = imageSearchApi.queryPictures('\'' + query + '\'');
        BingSearchData data = response.getData();

        if (data != null && data.getResults() != null && !data.getResults().isEmpty()) {
            final List<BingSearchResultItem> searchResults = data.getResults();
            log.info("> {}: {} results", query, searchResults.size());
            return searchResults.stream()
                    .filter(pic ->
                            !ignoredTypes.contains(pic.getContentType()) &&
                            !FilenameUtils.getExtension(pic.getMediaUrl()).isEmpty()
                    )
                    .collect(Collectors.toList());
        }
        log.error("Bad Bing response: {}", data);
        return null;
    }



}
