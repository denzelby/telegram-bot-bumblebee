package com.github.bumblebee.bot

import com.github.bumblebee.util.logger
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.annotation.PostConstruct

@Configuration
class TimeZoneConfig(private val config: BumblebeeConfig) {

    @PostConstruct
    fun setupTimezone() {
        val timeZone = TimeZone.getTimeZone(config.timezone ?: "UTC")
        logger<TimeZoneConfig>().info("Using timezone: $timeZone")
        TimeZone.setDefault(timeZone)
    }
}