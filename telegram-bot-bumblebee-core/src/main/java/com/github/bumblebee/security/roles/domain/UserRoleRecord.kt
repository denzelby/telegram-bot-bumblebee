package com.github.bumblebee.security.roles.domain

import com.github.bumblebee.security.UserRole

import javax.persistence.*

@Entity
@Table(name = "BB_USER_ROLES")
class UserRoleRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var userId: Long? = null

    @Enumerated(EnumType.STRING)
    var role: UserRole? = null

    @Suppress("unused")
    constructor()

    constructor(userId: Long, role: UserRole) {
        this.userId = userId
        this.role = role
    }

    override fun toString(): String = "UserRoleRecord(id=$id, userId=$userId, role=$role)"
}
