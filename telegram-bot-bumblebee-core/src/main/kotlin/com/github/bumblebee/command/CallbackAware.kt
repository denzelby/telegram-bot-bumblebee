package com.github.bumblebee.command

import com.github.telegram.domain.CallbackQuery


interface CallbackAware {
    fun callbackId(): String
    fun onCallback(data: String, callbackQuery: CallbackQuery)

    /**
     * Prepend callback id to callback data (so update could be routed to component later)
     */
    fun callbackData(data: String) = "${callbackId()}$$data"

    companion object {
        fun parse(callbackData: String): Pair<String, String>? = callbackData
            .split('$', limit = 2)
            .takeIf { it.size == 2 }
            ?.let { (it.first() to it.last()) }
    }
}