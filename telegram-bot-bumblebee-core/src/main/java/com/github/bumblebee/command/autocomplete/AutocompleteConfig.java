package com.github.bumblebee.command.autocomplete;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misha on 04.06.2016.
 */

@Configuration
@ConfigurationProperties(prefix = "autocomplete")
public class AutocompleteConfig {

    private List<String> template = new ArrayList<String>();

    public List<String> getTemplate() {
        return template;
    }

    public void setTemplate(List<String> template) {
        this.template = template;
    }
}
