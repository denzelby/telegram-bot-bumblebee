package com.github.bumblebee.command.imagesearch.provider.google;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.api")
public class GoogleCustomSearchConfig {

    private String key;
    private String customSearchId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCustomSearchId() {
        return customSearchId;
    }

    public void setCustomSearchId(String customSearchId) {
        this.customSearchId = customSearchId;
    }
}
