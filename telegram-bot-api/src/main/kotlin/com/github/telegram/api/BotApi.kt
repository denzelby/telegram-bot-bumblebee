package com.github.telegram.api


import com.github.telegram.domain.*
import feign.Headers
import feign.Param
import feign.RequestLine

data class Response<out T>(val result: T?, val ok: Boolean, val errorCode: Int?, val description: String?)

interface BotApi {
    @RequestLine("GET /getMe")
    fun getMe(): Response<User>

    @RequestLine("POST /sendMessage")
    @Headers("Content-type: application/json")
    fun sendMessage(
            @Param("chat_id") chatId: String,
            @Param("text") text: String,
            @Param("parse_mode") parseMode: String? = null,
            @Param("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    /**
     * Convenience method for Java (as we cannot use @JvmOverloads on interface)
     */
    @RequestLine("POST /sendMessage")
    @Headers("Content-type: application/json")
    fun sendMessage(
            @Param("chat_id") chatId: Long,
            @Param("text") text: String
    ): Response<Message>

    /**
     * Convenience method for Java (as we cannot use @JvmOverloads on interface)
     */
    @RequestLine("POST /sendMessage")
    @Headers("Content-type: application/json")
    fun sendMessage(
            @Param("chat_id") chatId: Long,
            @Param("text") text: String,
            @Param("reply_to_message_id") replyToMessageId: Long
    ): Response<Message>

    @RequestLine("POST /sendMessage")
    @Headers("Content-type: application/json")
    fun sendMessage(
            @Param("chat_id") chatId: Long,
            @Param("text") text: String,
            @Param("parse_mode") parseMode: String? = null,
            @Param("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /forwardMessage")
    @Headers("Content-type: application/json")
    fun forwardMessage(
            @Param("chat_id") chatId: String,
            @Param("from_chat_id") fromChatId: String,
            @Param("message_id") messageId: Long,
            @Param("disable_notification") disableNotification: Boolean? = null
    ): Response<Message>

    @RequestLine("POST /forwardMessage")
    @Headers("Content-type: application/json")
    fun forwardMessage(
            @Param("chat_id") chatId: Long,
            @Param("from_chat_id") fromChatId: String,
            @Param("message_id") messageId: Long,
            @Param("disable_notification") disableNotification: Boolean? = null
    ): Response<Message>

    @RequestLine("POST /editMessageText")
    @Headers("Content-type: application/json")
    fun editMessageText(
        @Param("chat_id") chatId: Long,
        @Param("message_id") messageId: Long,
        @Param("text") text: String,
        @Param("parse_mode") parseMode: String? = null,
        @Param("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
        @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /editMessageText")
    @Headers("Content-type: application/json")
    fun editMessageText(
        @Param("inline_message_id") inlineMessageId: String,
        @Param("text") text: String,
        @Param("parse_mode") parseMode: String? = null,
        @Param("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
        @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /editMessageCaption")
    @Headers("Content-type: application/json")
    fun editMessageCaption(
        @Param("chat_id") chatId: Long,
        @Param("message_id") messageId: Long,
        @Param("caption") caption: String,
        @Param("parse_mode") parseMode: String? = null,
        @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /editMessageCaption")
    @Headers("Content-type: application/json")
    fun editMessageCaption(
        @Param("inline_message_id") inlineMessageId: Long,
        @Param("caption") caption: String,
        @Param("parse_mode") parseMode: String? = null,
        @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /editMessageReplyMarkup")
    @Headers("Content-type: application/json")
    fun editMessageReplyMarkup(
        @Param("chat_id") chatId: Long,
        @Param("message_id") messageId: Long,
        @Param("reply_markup") replyMarkup: ReplyMarkup?
    ): Response<Message>

    @RequestLine("POST /editMessageReplyMarkup")
    @Headers("Content-type: application/json")
    fun editMessageReplyMarkup(
        @Param("inline_message_id") inlineMessageId: Long,
        @Param("reply_markup") replyMarkup: ReplyMarkup?
    ): Response<Message>

    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: application/json")
    fun sendPhoto(
            @Param("chat_id") chatId: String,
            @Param("photo") fileId: String,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: application/json")
    fun sendPhoto(
            @Param("chat_id") chatId: Long,
            @Param("photo") fileId: String,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: multipart/form-data")
    fun sendPhoto(@Param("chat_id") chatId: Long,
                  @Param("photo") photo: InputFile,
                  @Param("caption") caption: String? = null,
                  @Param("disable_notification") disableNotification: Boolean? = null,
                  @Param("reply_to_message_id") replyToMessageId: Long? = null,
                  @Param("reply_markup") replyMarkup: ReplyMarkup? = null): Response<Message>

    @RequestLine("POST /sendAudio")
    @Headers("Content-type: application/json")
    fun sendAudio(
            @Param("chat_id") chatId: String,
            @Param("audio") fileId: String,
            @Param("duration") duration: Int? = null,
            @Param("performer") performer: String? = null,
            @Param("title") title: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendAudio")
    @Headers("Content-type: application/json")
    fun sendAudio(
            @Param("chat_id") chatId: Long,
            @Param("audio") fileId: String,
            @Param("duration") duration: Int? = null,
            @Param("performer") performer: String? = null,
            @Param("title") title: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendAudio")
    fun sendAudio(
            @Param("chat_id") chatId: String,
            @Param("audio") audio: InputFile,
            @Param("duration") duration: Int? = null,
            @Param("performer") performer: String? = null,
            @Param("title") title: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendAudio")
    @Headers("Content-type: multipart/form-data")
    fun sendAudio(
            @Param("chat_id") chatId: Long,
            @Param("audio") audio: InputFile,
            @Param("duration") duration: Int? = null,
            @Param("performer") performer: String? = null,
            @Param("title") title: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendDocument")
    @Headers("Content-type: application/json")
    fun sendDocument(
            @Param("chat_id") chatId: String,
            @Param("document") fileId: String,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendDocument")
    @Headers("Content-type: application/json")
    fun sendDocument(
            @Param("chat_id") chatId: Long,
            @Param("document") fileId: String,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendDocument")
    @Headers("Content-type: multipart/form-data")
    fun sendDocument(
            @Param("chat_id") chatId: String,
            @Param("document") document: InputFile,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendDocument")
    @Headers("Content-type: multipart/form-data")
    fun sendDocument(
            @Param("chat_id") chatId: Long,
            @Param("document") document: InputFile,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendSticker")
    @Headers("Content-type: application/json")
    fun sendSticker(
            @Param("chat_id") chatId: String,
            @Param("sticker") fileId: String,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    /**
     * Convenience method for Java (as we cannot use @JvmOverloads on interface)
     */
    @RequestLine("POST /sendSticker")
    @Headers("Content-type: application/json")
    fun sendSticker(
            @Param("chat_id") chatId: Long,
            @Param("sticker") fileId: String
    ): Response<Message>

    @RequestLine("POST /sendSticker")
    @Headers("Content-type: application/json")
    fun sendSticker(
            @Param("chat_id") chatId: Long,
            @Param("sticker") fileId: String,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendSticker")
    @Headers("Content-type: multipart/form-data")
    fun sendSticker(
            @Param("chat_id") chatId: String,
            @Param("sticker") sticker: InputFile,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendSticker")
    @Headers("Content-type: multipart/form-data")
    fun sendSticker(
            @Param("chat_id") chatId: Long,
            @Param("sticker") sticker: InputFile,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: application/json")
    fun sendVideo(
            @Param("chat_id") chatId: String,
            @Param("video") fileId: String,
            @Param("duration") duration: Int? = null,
            @Param("width") width: Int? = null,
            @Param("height") height: Int? = null,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: application/json")
    fun sendVideo(
            @Param("chat_id") chatId: Long,
            @Param("video") fileId: String,
            @Param("duration") duration: Int? = null,
            @Param("width") width: Int? = null,
            @Param("height") height: Int? = null,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: multipart/form-data")
    fun sendVideo(
            @Param("chat_id") chatId: String,
            @Param("video") video: InputFile,
            @Param("duration") duration: Int? = null,
            @Param("width") width: Int? = null,
            @Param("height") height: Int? = null,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: multipart/form-data")
    fun sendVideo(
            @Param("chat_id") chatId: Long,
            @Param("video") video: InputFile,
            @Param("duration") duration: Int? = null,
            @Param("width") width: Int? = null,
            @Param("height") height: Int? = null,
            @Param("caption") caption: String? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: application/json")
    fun sendVoice(
            @Param("chat_id") chatId: String,
            @Param("voice") fileId: String,
            @Param("duration") duration: Int? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: application/json")
    fun sendVoice(
            @Param("chat_id") chatId: Long,
            @Param("voice") fileId: String,
            @Param("duration") duration: Int? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: multipart/form-data")
    fun sendVoice(
            @Param("chat_id") chatId: String,
            @Param("voice") voice: InputFile,
            @Param("duration") duration: Int? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: multipart/form-data")
    fun sendVoice(
            @Param("chat_id") chatId: Long,
            @Param("voice") voice: InputFile,
            @Param("duration") duration: Int? = null,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Long? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendLocation")
    @Headers("Content-type: application/json")
    fun sendLocation(
            @Param("chat_id") chatId: String,
            @Param("latitude") latitude: Float,
            @Param("longitude") longitude: Float,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendLocation")
    @Headers("Content-type: application/json")
    fun sendLocation(
            @Param("chat_id") chatId: Long,
            @Param("latitude") latitude: Float,
            @Param("longitude") longitude: Float,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVenue")
    @Headers("Content-type: application/json")
    fun sendVenue(
            @Param("chat_id") chatId: String,
            @Param("latitude") latitude: Float,
            @Param("longitude") longitude: Float,
            @Param("title") title: String,
            @Param("address") address: String,
            @Param("foursquare_id") foursquareId: String?,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendVenue")
    @Headers("Content-type: application/json")
    fun sendVenue(
            @Param("chat_id") chatId: Long,
            @Param("latitude") latitude: Float,
            @Param("longitude") longitude: Float,
            @Param("title") title: String,
            @Param("address") address: String,
            @Param("foursquare_id") foursquareId: String?,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendContact")
    @Headers("Content-type: application/json")
    fun sendContact(
            @Param("chat_id") chatId: String,
            @Param("phone_number") phoneNumber: String,
            @Param("first_name") firstName: String,
            @Param("last_name") lastName: String?,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendContact")
    @Headers("Content-type: application/json")
    fun sendContact(
            @Param("chat_id") chatId: Long,
            @Param("phone_number") phoneNumber: String,
            @Param("first_name") firstName: String,
            @Param("last_name") lastName: String?,
            @Param("disable_notification") disableNotification: Boolean? = null,
            @Param("reply_to_message_id") replyToMessageId: Int? = null,
            @Param("reply_markup") replyMarkup: ReplyMarkup? = null
    ): Response<Message>

    @RequestLine("POST /sendChatAction")
    @Headers("Content-type: application/json")
    fun sendChatAction(
            @Param("chat_id") chatId: String,
            @Param("action") action: ChatAction
    ): Response<Boolean>

    @RequestLine("POST /sendChatAction")
    @Headers("Content-type: application/json")
    fun sendChatAction(
            @Param("chat_id") chatId: Long,
            @Param("action") action: ChatAction
    ): Response<Boolean>

    @RequestLine("GET /getUserProfilePhotos?user_id={user_id}&offset={offset}&limit={limit}")
    fun getUserProfilePhotos(
            @Param("user_id") userId: Long,
            @Param("offset") offset: Int,
            @Param("limit") limit: Int
    ): Response<UserProfilePhotos>

    @RequestLine("GET /getFile?file_id={file_id}")
    fun getUserProfilePhotos(
            @Param("file_id") fileId: String
    ): Response<File>

    @RequestLine("POST /kickChatMember")
    @Headers("Content-type: application/json")
    fun kickChatMember(
            @Param("chat_id") chatId: String,
            @Param("user_id") userId: Long
    ): Response<Boolean>

    @RequestLine("POST /leaveChat")
    @Headers("Content-type: application/json")
    fun leaveChat(
            @Param("chat_id") chatId: String
    ): Response<Boolean>

    @RequestLine("POST /unbanChatMember")
    @Headers("Content-type: application/json")
    fun unbanChatMember(
            @Param("chat_id") chatId: String,
            @Param("user_id") userId: Long
    ): Response<Boolean>

    @RequestLine("GET /getChat?chat_id={chat_id}")
    fun getChat(
            @Param("chat_id") chatId: String
    ): Response<Chat>

    @RequestLine("GET /getChatAdministrators?chat_id={chat_id}")
    fun getChatAdministrators(
            @Param("chat_id") chatId: Long
    ): Response<List<ChatMember>>

    @RequestLine("GET /getChatMembersCount?chat_id={chat_id}")
    fun getChatMembersCount(
            @Param("chat_id") chatId: Long
    ): Response<Int>

    @RequestLine("GET /getChatMember?chat_id={chat_id}&user_id={user_id}")
    fun getChatMember(
            @Param("chat_id") chatId: Long,
            @Param("user_id") userId: Long
    ): Response<ChatMember>

    @RequestLine("POST /answerCallbackQuery")
    @Headers("Content-type: application/json")
    fun answerCallbackQuery(
            @Param("callback_query_id") callbackQueryId: String,
            @Param("text") text: String? = null,
            @Param("show_alert") showAlert: Boolean? = null
    ): Response<Boolean>

    /**
     * Use this method to receive incoming updates using long polling (wiki).
     *
     * @param offset Identifier of the first update to be returned.
     *        Must be greater by one than the highest among the identifiers of previously received updates.
     *        By default, updates starting with the earliest unconfirmed update are returned.
     *        An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id.
     *        The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates queue.
     *        All previous updates will be forgotten.
     * @param limit
     * @param timeout
     *
     * @return an array of [Update] objects.
     */
    @RequestLine("GET /getUpdates?offset={offset}&limit={limit}&timeout={timeout}")
    fun getUpdates(
            @Param("offset") offset: Long,
            @Param("limit") limit: Int = 100,
            @Param("timeout") timeout: Int = 0
    ): Response<List<Update>>

    /**
     * Use this method to specify a url and receive incoming updates via an outgoing webhook.
     *
     * @param url HTTPS url to send updates to. Use an empty string to remove webhook integration.
     * @param certificate Upload your public key certificate so that the root certificate in use can be checked.
     */
    @RequestLine("POST /setWebhook")
    @Headers("Content-type: multipart/form-data")
    fun setWebhook(
            @Param("url") url: String,
            @Param("certificate") certificate: InputFile? = null
    ): Response<Boolean>

    @RequestLine("POST /setWebhook")
    fun removeWebhook(): Response<Boolean>
}