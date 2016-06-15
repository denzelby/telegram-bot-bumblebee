package com.github.bumblebee.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fare on 15.06.2016.
 */

@Configuration
@ConfigurationProperties(prefix = "adminaccess")
public class BumblebeeSecurityConfig {

    private List<String> adminIds = new ArrayList<>();

    public List<String> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(List<String> adminIds) {
        this.adminIds = adminIds;
    }
}
