package com.github.bumblebee.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import telegram.polling.HandlerRegistry;
import telegram.polling.UpdateHandler;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class BumblebeeBotCommands {

    private static final Logger log = LoggerFactory.getLogger(BumblebeeBotCommands.class);

    @Autowired
    private BumblebeeConfig config;

    @Resource
    private List<UpdateHandler> handlers;

    @Bean
    public HandlerRegistry createCommandRegistry() {
        HandlerRegistry registry = new HandlerRegistry();

        for (UpdateHandler handler : handlers) {
            String command = handler.getClass().getSimpleName();
            String aliases = config.getCommands().get(command);

            if (!StringUtils.isEmpty(aliases)) {
                registry.register(handler, aliases.split("\\s"));
                log.info("Registered command: {}", command);
            } else {
                log.warn("No command mapping found for {}", command);
            }
        }
        return registry;
    }
}
