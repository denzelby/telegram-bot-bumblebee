package com.github.bumblebee.command.youtube;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConfigurationProperties(prefix = "youtube.api")
public class YoutubeSearchConfig {

    private String projectName;
    private String key;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAvailable() {

        return !StringUtils.isEmpty(projectName) && !StringUtils.isEmpty(key);
    }
}
