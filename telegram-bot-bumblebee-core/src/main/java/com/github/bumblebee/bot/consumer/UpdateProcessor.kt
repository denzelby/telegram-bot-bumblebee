package com.github.bumblebee.bot.consumer

import com.github.bumblebee.util.loggerFor
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Process update via TelegramUpdateRouter using thread pool.
 * Updates from the same chats are guaranteed to be dispatched to the same threads.
 */
@Component
class UpdateProcessor(private val updateRouter: TelegramUpdateRouter) {

    private val executors: List<ExecutorService> by lazy {
        loggerFor<UpdateProcessor>().info("Initializing long polling service executors")
        (0..executorsCount).map { Executors.newSingleThreadExecutor() }
    }

    fun process(update: Update) {
        // updates from the same chats goes to the same executor
        val executorIndex = update.senderId.hashCode() % executors.size
        executors[executorIndex].submit {
            updateRouter.route(update)
        }
    }

    companion object {
        private val executorsCount = 4
    }
}