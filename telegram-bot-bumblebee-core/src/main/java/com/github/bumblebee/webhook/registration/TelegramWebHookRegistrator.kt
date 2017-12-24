package com.github.bumblebee.webhook.registration

import com.github.telegram.api.BotApi
import com.github.telegram.api.Response
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File

@Service
class TelegramWebHookRegistrator(private val botApi: BotApi) {

    fun registerWebHook(url: String, certificatePath: String): Boolean {
        log.info("Registering webhook: {}, certificate path: {}", url + URL_POSTFIX, certificatePath)
        return setWebHook(url + URL_POSTFIX, certificatePath)
    }

    fun removeWebHook(): Boolean {
        log.info("Removing webhook...")
        return setWebHook("", null)
    }

    private fun setWebHook(hookUrl: String, filePath: String?): Boolean {

        val certificate = if (filePath != null) File(filePath) else null
        val callResponse: retrofit2.Response<Response<Unit>>
        return if (certificate != null) {
            callResponse = botApi.setWebhook(hookUrl, certificate).execute()
            val response = callResponse.body()
            log.info("Webhook registration: success = {}, description = {}", response!!.ok,
                    response.description)
            response.ok
        } else {
            // todo: this is not correct at all
            botApi.removeWebhook()
            true
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(TelegramWebHookRegistrator::class.java)
        private val URL_POSTFIX = "/webhook"
    }
}
