package com.github.telegram.domain

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents an incoming update.
 * Only *one* of the optional parameters can be present in any given update.
 * 
 * @property updateId The update‘s unique identifier.
 *        Update identifiers start from a certain positive number and increase sequentially.
 * @property message New incoming message of any kind — text, photo, sticker, etc.
 * @property editedMessage New version of a message that is known to the bot and was edited.
 * @property inlineQuery New incoming inline query.
 * @property chosenInlineResult The result of an inline query that was chosen by a user and sent to their chat partner.
 * @property callbackQuery New incoming callback query.
 */
data class Update(
        @Name("update_id") val updateId: Long,
        @Name("message") val message: Message?,
        @Name("edited_message") val editedMessage: Message?,
        @Name("channel_post") val channelPost: Message?,
        @Name("edited_channel_post") val editedChannelPost: Message?,
        @Name("inline_query") val inlineQuery: InlineQuery?,
        @Name("chosen_inline_result") val chosenInlineResult: ChosenInlineResult?,
        @Name("callback_query") val callbackQuery: CallbackQuery?) {
    val senderId: Long
        get() {
            return when {
                message != null -> message.chat.id
                editedMessage != null -> editedMessage.chat.id
                channelPost != null -> channelPost.chat.id
                editedChannelPost != null -> editedChannelPost.chat.id
                inlineQuery != null -> inlineQuery.from.id
                chosenInlineResult != null -> chosenInlineResult.from.id
                callbackQuery != null -> callbackQuery.from.id
                else -> throw IllegalStateException("Cannot evaluate sender for update: $this")
            }
        }
}