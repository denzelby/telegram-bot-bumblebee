package com.github.bumblebee.bot.consumer

import com.github.bumblebee.command.CallbackAware
import java.util.*

class HandlerRegistry {

    /**
     * Command mapping
     */
    private val commands = HashMap<String, UpdateHandler>()

    /**
     * Callback id to handler
     */
    private val callbackHandlers = HashMap<String, CallbackAware>()

    /**
     * Handler chain
     */
    private val handlers = ArrayList<UpdateHandler>()

    val handlerChain: List<UpdateHandler>
        get() = handlers

    fun register(handler: UpdateHandler, aliases: List<String>): HandlerRegistry {
        aliases.forEach { alias -> commands[alias] = handler }
        registerCallbackAware(handler)
        return this
    }

    fun register(handler: UpdateHandler): HandlerRegistry {
        handlers.add(handler)
        registerCallbackAware(handler)
        return this
    }

    private fun registerCallbackAware(handler: UpdateHandler) {
        if (handler is CallbackAware) {
            callbackHandlers.putIfAbsent(handler.callbackId(), handler)?.let { registeredHandler ->
                throw IllegalStateException(
                    "More than one handler associated with single id '${handler.callbackId()}': " +
                            "${handler.javaClass.simpleName}, ${registeredHandler.javaClass.simpleName}"
                )
            }
        }
    }

    fun getByAlias(alias: String?): UpdateHandler? = commands[alias]

    fun getByCallbackId(callbackId: String): CallbackAware? = callbackHandlers[callbackId]
}
