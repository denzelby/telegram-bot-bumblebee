package com.github.telegram.domain

import com.google.gson.annotations.SerializedName as Name

/**
 * This object contains information about one member of the chat.

 * @property user Information about the user.
 * @property status The member's status in the chat. Can be “creator”, “administrator”, “member”, “left” or “kicked”.
 * @property untilDate Restricted and kicked only. Date when restrictions will be lifted for this user, unix time
 * @property canBeEdited Administrators only. True, if the bot is allowed to edit administrator privileges of that user
 * @property canChangeInfo Administrators only. True, if the administrator can change the chat title, photo and other settings
 * @property canPostMessages Administrators only. True, if the administrator can post in the channel, channels only
 * @property canEditMessages Administrators only. True, if the administrator can edit messages of other users and can pin messages, channels only
 * @property canDeleteMessages Administrators only. True, if the administrator can delete messages of other users
 * @property canInviteUsers Administrators only. True, if the administrator can invite new users to the chat
 * @property canRestrictMembers Administrators only. True, if the administrator can restrict, ban or unban chat members
 * @property canPinMessages Administrators only. True, if the administrator can pin messages, supergroups only
 * @property canPromoteMembers Administrators only. True, if the administrator can add new administrators with a subset of his own privileges or demote administrators that he has promoted, directly or indirectly (promoted by administrators that were appointed by the user)
 * @property canSendMessages Restricted only. True, if the user can send text messages, contacts, locations and venues
 * @property canSendMediaMessages Restricted only. True, if the user can send audios, documents, photos, videos, video notes and voice notes, implies canSendMessages
 * @property canSendOtherMessages Restricted only. True, if the user can send animations, games, stickers and use inline bots, implies canSendMediaMessages
 * @property canAddWebPagePreviews Restricted only. True, if user may add web page previews to his messages, implies canSendMediaMessages
 */
data class ChatMember(
        @Name("user") val user: User,
        @Name("status") val status: String,
        @Name("until_date") val untilDate: Int?,
        @Name("can_be_edited") val canBeEdited: Boolean?,
        @Name("can_change_info") val canChangeInfo: Boolean?,
        @Name("can_post_messages") val canPostMessages: Boolean?,
        @Name("can_edit_messages") val canEditMessages: Boolean?,
        @Name("can_delete_messages") val canDeleteMessages: Boolean?,
        @Name("can_invite_users") val canInviteUsers: Boolean?,
        @Name("can_restrict_members") val canRestrictMembers: Boolean?,
        @Name("can_pin_messages") val canPinMessages: Boolean?,
        @Name("can_promote_members") val canPromoteMembers: Boolean?,
        @Name("can_send_messages") val canSendMessages: Boolean?,
        @Name("can_send_media_messages") val canSendMediaMessages: Boolean?,
        @Name("can_send_other_messages") val canSendOtherMessages: Boolean?,
        @Name("can_add_web_page_previews") val canAddWebPagePreviews: Boolean?
)