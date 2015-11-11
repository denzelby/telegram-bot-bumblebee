package telegram.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import telegram.domain.*;
import telegram.domain.request.ChatAction;
import telegram.domain.request.InputFile;
import telegram.domain.request.ParseMode;
import telegram.domain.request.keyboard.Keyboard;
import telegram.impl.EnumExpander;
import telegram.impl.ParseModeExpander;

import java.util.List;

/**
 * Telegram bot API.
 * See https://core.telegram.org/bots/api
 */
public interface BotApi {

    // About Bot
    @RequestLine("GET /getMe")
    BasicResponse<User> getMe();

    // Get updates
    @RequestLine("GET /getUpdates?offset={offset}&limit={limit}&timeout={timeout}")
    BasicResponse<List<Update>> getUpdates(@Param("offset") long offset,
                                           @Param("limit") int limit,
                                           @Param("timeout") int timeout);

    @RequestLine("GET /getUpdates?offset={offset})")
    BasicResponse<List<Update>> getUpdates(@Param("offset") Long offset);

    @RequestLine("GET /getUpdates")
    BasicResponse<List<Update>> getUpdates();

    // Messsages
    @RequestLine("POST /sendMessage")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendMessage(@Param("chat_id") Integer chatId,
                                       @Param("text") String text,
                                       @Param(value = "parse_mode", expander = ParseModeExpander.class) ParseMode parseMode,
                                       @Param("disable_web_page_preview") Boolean disableWebPagePreview,
                                       @Param("reply_to_message_id") Integer replyToMessageId,
                                       @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendMessage")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendMessage(@Param("chat_id") Integer chatId, @Param("text") String text);

    @RequestLine("POST /forwardMessage")
    @Headers("Content-type: application/json")
    BasicResponse<Message> forwardMessage(@Param("chat_id") String chatId,
                                          @Param("from_chat_id") String fromChatId,
                                          @Param("message_id") Integer messageId);

    // Get user profile photos
    @RequestLine("GET /getUserProfilePhotos?user_id={user_id}&offset={offset}&limit={limit}")
    BasicResponse<UserProfilePhotos> getUserProfilePhotos(@Param("user_id") Integer userId,
                                                          @Param("offset") Integer offset,
                                                          @Param("limit") Integer limit);

    @RequestLine("GET /getUserProfilePhotos?user_id={user_id}")
    BasicResponse<UserProfilePhotos> getUserProfilePhotos(@Param("user_id") Integer userId);

    // Chat actions
    @RequestLine("POST /sendChatAction")
    @Headers("Content-type: application/json")
    BasicResponse<Boolean> sendChatAction(@Param("chat_id") String chatId,
                                          @Param(value = "action", expander = EnumExpander.class) ChatAction chatAction);

    // Location
    @RequestLine("POST /sendLocation")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendLocation(@Param("chat_id") String chatId,
                                        @Param("latitude") float latitude,
                                        @Param("longitude") float longitude,
                                        @Param("reply_to_message_id") Integer replyToMessageId,
                                        @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendLocation")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendLocation(@Param("chat_id") String chatId,
                                        @Param("latitude") float latitude,
                                        @Param("longitude") float longitude);

    // Send photo
    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendPhoto(@Param("chat_id") String chatId,
                                     @Param("photo") String photoId,
                                     @Param("caption") String caption,
                                     @Param("reply_to_message_id") Integer replyToMessageId,
                                     @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendPhoto(@Param("chat_id") String chatId,
                                     @Param("photo") String photoId,
                                     @Param("caption") String caption);

    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendPhoto(@Param("chat_id") String chatId, @Param("photo") String photoId);


    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendPhoto(@Param("chat_id") String chatId,
                                     @Param("photo") InputFile photo,
                                     @Param("caption") String caption,
                                     @Param("reply_to_message_id") Integer replyToMessageId,
                                     @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendPhoto(@Param("chat_id") String chatId,
                                     @Param("photo") InputFile photo,
                                     @Param("caption") String caption);

    @RequestLine("POST /sendPhoto")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendPhoto(@Param("chat_id") String chatId, @Param("photo") InputFile photo);


    // Audio
    @RequestLine("POST /sendAudio")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendAudio(@Param("chat_id") String chatId,
                                     @Param("audio") String audioId,
                                     @Param("duration") Integer duration,
                                     @Param("performer") String performer,
                                     @Param("title") String title,
                                     @Param("reply_to_message_id") Integer replyToMessageId,
                                     @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendAudio")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendAudio(@Param("chat_id") String chatId,
                                     @Param("audio") String audioId,
                                     @Param("duration") Integer duration,
                                     @Param("performer") String performer,
                                     @Param("title") String title);

    @RequestLine("POST /sendAudio")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendAudio(@Param("chat_id") String chatId, @Param("audio") String audioId);

    @RequestLine("POST /sendAudio")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendAudio(@Param("chat_id") String chatId,
                                     @Param("audio") InputFile audio,
                                     @Param("duration") Integer duration,
                                     @Param("performer") String performer,
                                     @Param("title") String title,
                                     @Param("reply_to_message_id") Integer replyToMessageId,
                                     @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendAudio")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendAudio(@Param("chat_id") String chatId,
                                     @Param("audio") InputFile audio,
                                     @Param("duration") Integer duration,
                                     @Param("performer") String performer,
                                     @Param("title") String title);

    @RequestLine("POST /sendAudio")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendAudio(@Param("chat_id") String chatId, @Param("audio") InputFile audio);

    // Documents
    @RequestLine("POST /sendDocument")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendDocument(@Param("chat_id") String chatId,
                                        @Param("document") String documentId,
                                        @Param("reply_to_message_id") Integer replyToMessageId,
                                        @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendDocument")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendDocument(@Param("chat_id") String chatId, @Param("document") String documentId);

    @RequestLine("POST /sendDocument")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendDocument(@Param("chat_id") String chatId,
                                        @Param("document") InputFile document,
                                        @Param("reply_to_message_id") Integer replyToMessageId,
                                        @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendDocument")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendDocument(@Param("chat_id") String chatId, @Param("document") InputFile document);

    // Stickers
    @RequestLine("POST /sendSticker")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendSticker(@Param("chat_id") String chatId,
                                       @Param("sticker") String stickerId,
                                       @Param("reply_to_message_id") Integer replyToMessageId,
                                       @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendSticker")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendSticker(@Param("chat_id") String chatId, @Param("sticker") String stickerId);

    @RequestLine("POST /sendSticker")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendSticker(@Param("chat_id") String chatId,
                                       @Param("sticker") InputFile sticker,
                                       @Param("reply_to_message_id") Integer replyToMessageId,
                                       @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendSticker")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendSticker(@Param("chat_id") String chatId, @Param("sticker") InputFile sticker);

    // Video
    @RequestLine("POST /sendVideo")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendVideo(@Param("chat_id") String chatId,
                                     @Param("video") String videoId,
                                     @Param("duration") Integer duration,
                                     @Param("caption") String caption,
                                     @Param("reply_to_message_id") Integer replyToMessageId,
                                     @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendVideo(@Param("chat_id") String chatId,
                                     @Param("video") String videoId,
                                     @Param("duration") Integer duration,
                                     @Param("caption") String caption);

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendVideo(@Param("chat_id") String chatId, @Param("video") String videoId);

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendVideo(@Param("chat_id") String chatId,
                                     @Param("video") InputFile video,
                                     @Param("duration") Integer duration,
                                     @Param("caption") String caption,
                                     @Param("reply_to_message_id") Integer replyToMessageId,
                                     @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendVideo(@Param("chat_id") String chatId,
                                     @Param("video") InputFile video,
                                     @Param("duration") Integer duration,
                                     @Param("caption") String caption);

    @RequestLine("POST /sendVideo")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendVideo(@Param("chat_id") String chatId, @Param("video") InputFile video);

    // Voice
    @RequestLine("POST /sendVoice")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendVoice(@Param("chat_id") String chatId,
                                     @Param("voice") String voiceId,
                                     @Param("duration") Integer duration,
                                     @Param("reply_to_message_id") Integer replyToMessageId,
                                     @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendVoice(@Param("chat_id") String chatId,
                                     @Param("voice") String voiceId,
                                     @Param("duration") Integer duration);

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: application/json")
    BasicResponse<Message> sendVoice(@Param("chat_id") String chatId, @Param("voice") String voiceId);

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendVoice(@Param("chat_id") String chatId,
                                     @Param("voice") InputFile voice,
                                     @Param("duration") Integer duration,
                                     @Param("reply_to_message_id") Integer replyToMessageId,
                                     @Param("reply_markup") Keyboard keyboard);

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendVoice(@Param("chat_id") String chatId,
                                     @Param("voice") InputFile voice,
                                     @Param("duration") Integer duration);

    @RequestLine("POST /sendVoice")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Message> sendVoice(@Param("chat_id") String chatId, @Param("voice") InputFile voice);

    // Webhook
    @RequestLine("POST /setWebhook")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Boolean> setWebhook(@Param("url") String url, @Param("certificate") InputFile certificate);

    @RequestLine("POST /setWebhook")
    @Headers("Content-type: multipart/form-data")
    BasicResponse<Boolean> setWebhook(@Param("url") String url);

    // Get file
    @RequestLine("GET /getFile?file_id={file_id}")
    BasicResponse<File> getFile(@Param("file_id") String fileId);
}
