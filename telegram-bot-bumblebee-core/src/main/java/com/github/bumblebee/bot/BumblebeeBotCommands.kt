package com.github.bumblebee.bot

import org.slf4j.LoggerFactory
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import telegram.polling.HandlerRegistry
import telegram.polling.UpdateHandler
import javax.annotation.Resource

@Configuration
open class BumblebeeBotCommands {

    @Autowired
    private lateinit var config: BumblebeeConfig

    @Resource
    private lateinit var handlers: List<UpdateHandler>

    @Bean
    open fun createCommandRegistry(): HandlerRegistry {

        val registry = HandlerRegistry()

        handlers.forEach { handler ->
            val command = AopProxyUtils.ultimateTargetClass(handler).simpleName
            val aliases = config.commands[command]

            if (aliases != null) {
                registry.register(handler, *aliases.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                log.info("Registered command {} with aliases {}", command, aliases)
            } else {
                registry.register(handler)
                log.info("Added to update handler chain: {}", command)
            }
        }

        return registry
    }

    companion object {
        private val log = LoggerFactory.getLogger(BumblebeeBotCommands::class.java)
    }
}