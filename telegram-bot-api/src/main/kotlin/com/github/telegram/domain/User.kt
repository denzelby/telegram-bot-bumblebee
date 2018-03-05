package com.github.telegram.domain

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents a Telegram user or bot.
 * 
 * @property id Unique identifier for this user or bot.
 * @property firstName User‘s or bot’s first name.
 * @property lastName User‘s or bot’s last name.
 * @property userName User‘s or bot’s username.
 */
data class User(
        @Name("id") val id: Long,
        @Name("is_bot") val isBot: Boolean,
        @Name("first_name") val firstName: String,
        @Name("last_name") val lastName: String?,
        @Name("username") val userName: String?,
        @Name("language_code") val languageCode: String?)