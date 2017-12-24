package com.github.bumblebee.webhook

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
open class JsonMapperConfig {

    @Autowired
    fun configureJackson(mapper: ObjectMapper) {
        mapper.propertyNamingStrategy = PropertyNamingStrategy.SnakeCaseStrategy()
    }
}
