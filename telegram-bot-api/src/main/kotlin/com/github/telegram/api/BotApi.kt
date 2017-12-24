package com.github.telegram.api

import com.github.telegram.domain.Message
import com.github.telegram.domain.ReplyMarkup
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import java.nio.file.Files

private fun inputFile(file: File, mimeType: String? = null): RequestBody {
    val contentType = MediaType.parse(mimeType ?: Files.probeContentType(file.toPath()))
    return RequestBody.create(contentType, file)
}
private fun inputFile(byteArray: ByteArray, mimeType: String): RequestBody {
    return RequestBody.create(MediaType.parse(mimeType), byteArray)
}

private val textPlainMime = MediaType.parse("text/plain")
private val applicationJsonMime = MediaType.parse("application/json")
private fun requestString(text: String) = RequestBody.create(textPlainMime, text)
private fun requestJson(text: String) = RequestBody.create(applicationJsonMime, text)

fun <T> Call<T>.async() {
    this.enqueue(object: retrofit2.Callback<T> {
        override fun onResponse(call: Call<T>?, response: retrofit2.Response<T>?) {
        }

        override fun onFailure(call: Call<T>?, t: Throwable?) {
        }
    })
}

class BotApi(coreApi: BotCoreApi): BotCoreApi by coreApi {

//    fun sendPhoto(
//            chatId: String,
//            photo: File,
//            caption: String? = null,
//            disableNotification: Boolean? = null,
//            replyToMessageId: Long? = null,
//            replyMarkup: ReplyMarkup? = null
//    ): Call<Response<Message>> {
//        return sendPhoto(
//                requestString(chatId),
//                inputFile(photo),
//                if (caption != null) requestString(caption) else null,
//                if (disableNotification != null) requestString(disableNotification.toString()) else null,
//                if (replyToMessageId != null) requestString(replyToMessageId.toString()) else null,
//                if (replyMarkup != null) requestJson(replyMarkup.toString()) else null
//        )
//    }

    fun sendPhoto(
            chatId: Long,
            photo: ByteArray,
            contentType: String,
            caption: String? = null,
            disableNotification: Boolean? = null,
            replyToMessageId: Long? = null,
            replyMarkup: ReplyMarkup? = null
    ): Call<Response<Message>> {
//        val filePart = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("photo", "bmw.png", inputFile(photo, "image/jpeg"))
//                .build()

        val filePart = inputFile(photo, contentType)
        return sendPhoto(
                requestString(chatId.toString()), filePart,
//                MultipartBody.Part.createFormData("photo", "bmw.png", inputFile(photo, "image/jpeg")),
                if (caption != null) requestString(caption) else null,
                if (disableNotification != null) requestString(disableNotification.toString()) else null,
                if (replyToMessageId != null) requestString(replyToMessageId.toString()) else null,
                if (replyMarkup != null) requestJson(replyMarkup.toString()) else null
        )
    }

//    fun sendPhoto(
//            chatId: Long,
//            photo: File,
//            caption: String? = null,
//            disableNotification: Boolean? = null,
//            replyToMessageId: Long? = null,
//            replyMarkup: ReplyMarkup? = null
//    ): Call<Response<Message>> {
//        return sendPhoto(
//                requestString(chatId.toString()),
//                inputFile(photo),
//                if (caption != null) requestString(caption) else null,
//                if (disableNotification != null) requestString(disableNotification.toString()) else null,
//                if (replyToMessageId != null) requestString(replyToMessageId.toString()) else null,
//                if (replyMarkup != null) requestJson(replyMarkup.toString()) else null
//        )
//    }

    fun setWebhook(url: String, certificate: File): Call<Response<Unit>> {
        return setWebhook(requestString(url), inputFile(certificate))
    }
}