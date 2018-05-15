package com.github.bumblebee.security.roles.dao

import com.github.bumblebee.security.roles.domain.UserRoleRecord
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRolesRepository : CrudRepository<UserRoleRecord, Long> {

    fun findByUserId(userId: Long): List<UserRoleRecord>

    fun deleteByUserId(userId: Long): Int
}
