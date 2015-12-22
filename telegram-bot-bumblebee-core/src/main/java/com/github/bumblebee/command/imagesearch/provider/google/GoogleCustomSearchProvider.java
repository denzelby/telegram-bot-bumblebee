package com.github.bumblebee.command.imagesearch.provider.google;

import com.github.bumblebee.command.imagesearch.domain.Image;
import com.github.bumblebee.command.imagesearch.domain.ImageProvider;
import com.github.bumblebee.command.imagesearch.provider.google.response.GoogleCustomSearchResponse;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoogleCustomSearchProvider implements ImageProvider {

    private static final Logger log = LoggerFactory.getLogger(GoogleCustomSearchProvider.class);

    private final GoogleSearchApi searchApi;
    private final GoogleCustomSearchConfig config;

    @Autowired
    public GoogleCustomSearchProvider(GoogleCustomSearchConfig config) {
        this.config = config;
        this.searchApi = Feign.builder()
                .decoder(new GsonDecoder())
                .logLevel(feign.Logger.Level.BASIC)
                .logger(new Slf4jLogger())
                .target(GoogleSearchApi.class, GoogleSearchApi.API_ROOT);
    }

    @Override
    public List<Image> search(String query) {
        GoogleCustomSearchResponse response = searchApi.queryPictures(query, config.getKey(), config.getCustomSearchId());
        if (response != null && !CollectionUtils.isEmpty(response.getItems())) {
            log.info("Google found {} results for: {}", response.getItems().size(), query);
            return response.getItems()
                    .stream()
                    .filter(item -> !FilenameUtils.getExtension(item.getLink()).isEmpty())
                    .collect(Collectors.toList());
        }
        return null;
    }

}
