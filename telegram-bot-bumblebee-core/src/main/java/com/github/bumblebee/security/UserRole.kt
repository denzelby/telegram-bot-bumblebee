package com.github.bumblebee.security

enum class UserRole(private val priority: Int) {

    SYSTEM(3), ADMIN(2), MODERATOR(1), USER(0), BANNED(-1);

    fun rolePriority(): Int {
        return priority
    }
}
