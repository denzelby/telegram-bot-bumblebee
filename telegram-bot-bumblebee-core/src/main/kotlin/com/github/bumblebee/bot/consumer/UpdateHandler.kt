package com.github.bumblebee.bot.consumer

import com.github.telegram.domain.Update

@FunctionalInterface
interface UpdateHandler {
    /**
     * Handle telegram update event
     * @param update update to process
     * @return true if update is consumed and should not be propagated through handler chain
     */
    fun onUpdate(update: Update): Boolean
}