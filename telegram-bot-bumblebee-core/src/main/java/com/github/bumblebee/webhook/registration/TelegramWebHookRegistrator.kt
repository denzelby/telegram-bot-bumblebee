package com.github.bumblebee.webhook.registration

import com.github.bumblebee.util.loggerFor
import com.github.telegram.api.BotApi
import com.github.telegram.api.InputFile
import org.springframework.stereotype.Service
import java.io.File

@Service
class TelegramWebHookRegistrator(private val botApi: BotApi) {

    fun registerWebHook(url: String, certificatePath: String?): Boolean {
        val webHookUrl = url + "/webhook"
        log.info("Registering webhook: {}, certificate path: {}", webHookUrl, certificatePath)

        val certificate = if (certificatePath != null) InputFile.document(File(certificatePath)) else null
        val response = botApi.setWebhook(webHookUrl, certificate)
        log.info("Webhook registration: result = {}, description = {}", response.result, response.description)
        return response.ok && response.result!!
    }

    fun removeWebHook(): Boolean {
        log.info("Removing webhook")
        return botApi.removeWebhook().ok
    }

    companion object {
        private val log = loggerFor<TelegramWebHookRegistrator>()
    }
}
