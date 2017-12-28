package com.github.bumblebee.bot.consumer

import java.util.*

class HandlerRegistry {

    /**
     * Command mapping
     */
    private val commands = HashMap<String, UpdateHandler>()

    /**
     * Handler chain
     */
    private val handlers = ArrayList<UpdateHandler>()

    val handlerChain: List<UpdateHandler>
        get() = handlers

    fun register(handler: UpdateHandler, aliases: List<String>): HandlerRegistry {

        aliases.forEach { alias -> commands.put(alias, handler) }
        return this
    }

    fun register(handler: UpdateHandler): HandlerRegistry {
        handlers.add(handler)
        return this
    }

    operator fun get(alias: String?): UpdateHandler? {
        return commands[alias]
    }
}
