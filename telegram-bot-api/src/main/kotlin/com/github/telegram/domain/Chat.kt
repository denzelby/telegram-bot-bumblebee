package com.github.telegram.domain

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents a chat.
 *
 * @property id Unique identifier for this chat.
 * @property type Type of chat, can be either “private”, “group”, “supergroup” or “channel”.
 * @property title Title, for channels and group chats.
 * @property userName Username, for private chats, supergroups and channels if available.
 * @property firstName First name of the other party in a private chat.
 * @property lastName Last name of the other party in a private chat.
 */
data class Chat(
        @Name("id") val id: Long,
        @Name("type") val type: ChatType,
        @Name("title") val title: String?,
        @Name("username") val userName: String?,
        @Name("first_name") val firstName: String?,
        @Name("last_name") val lastName: String?,
        @Name("all_members_are_administrators") val allMembersAreAdministrators: Boolean,
        @Name("photo") val photo: ChatPhoto?,
        @Name("description") val description: String?,
        @Name("invite_link") val inviteLink: String?,
        @Name("pinned_message") val pinnedMessage: Message?,
        @Name("sticker_set_name") val stickerSetName: String?,
        @Name("can_set_sticker_set") val canSetStickerSet: Boolean
)