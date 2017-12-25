package com.github.bumblebee.webhook.registration

import com.github.bumblebee.util.loggerFor
import com.github.telegram.api.BotApi
import com.github.telegram.api.InputFile
import org.springframework.stereotype.Service
import java.io.File

@Service
class TelegramWebHookRegistrator(private val botApi: BotApi) {

    fun registerWebHook(url: String, certificatePath: String?): Boolean {
        val webHookUrl = url + urlPostfix
        log.info("Registering webhook: {}, certificate path: {}", webHookUrl, certificatePath)
        return setWebHook(webHookUrl, certificatePath)
    }

    fun removeWebHook(): Boolean {
        log.info("Removing webhook...")
        return botApi.removeWebhook().ok
    }

    private fun setWebHook(hookUrl: String, filePath: String?): Boolean {
        val certificate = if (filePath != null) InputFile.document(File(filePath)) else null
        val response = botApi.setWebhook(hookUrl, certificate)
        log.info("Webhook registration: success = {}, description = {}", response.result, response.description)
        return response.ok
    }

    companion object {
        private val log = loggerFor<TelegramWebHookRegistrator>()
        private val urlPostfix = "/webhook"
    }
}
