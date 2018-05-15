package com.github.bumblebee.bot

import com.github.bumblebee.bot.consumer.HandlerRegistry
import com.github.bumblebee.bot.consumer.UpdateHandler
import com.github.bumblebee.util.logger
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.Resource

@Configuration
class BumblebeeBotCommands {

    @Autowired
    private lateinit var config: BumblebeeConfig

    @Resource
    private lateinit var handlers: List<UpdateHandler>

    @Bean
    fun createCommandRegistry(): HandlerRegistry {

        val registry = HandlerRegistry()

        handlers.forEach { handler ->
            val command = AopProxyUtils.ultimateTargetClass(handler).simpleName
            val aliases = config.commands[command]

            if (aliases != null) {
                registry.register(handler, aliases.split("\\s".toRegex()).dropLastWhile { it.isEmpty() })
                log.info("Registered command {} with aliases {}", command, aliases)
            } else {
                registry.register(handler)
                log.info("Added to update handler chain: {}", command)
            }
        }

        return registry
    }

    companion object {
        private val log = logger<BumblebeeBotCommands>()
    }
}