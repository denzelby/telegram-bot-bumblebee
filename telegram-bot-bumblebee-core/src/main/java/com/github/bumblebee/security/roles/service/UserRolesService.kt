package com.github.bumblebee.security.roles.service

import com.github.bumblebee.security.UserRole
import com.github.bumblebee.security.roles.dao.UserRolesRepository
import com.github.bumblebee.security.roles.domain.UserRoleRecord
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserRolesService(val rolesRepository: UserRolesRepository) {
    private var rolesInitialized: Boolean = false

    @Transactional(readOnly = true)
    fun getAllRoles(): Iterable<UserRoleRecord> = rolesRepository.findAll()

    fun isRolesInitialized(): Boolean {
        if (!rolesInitialized) {
            rolesInitialized = rolesRepository.count() > 0
        }
        return rolesInitialized
    }

    @Transactional(readOnly = true)
    fun hasPrivilege(userId: Long, minimumRole: UserRole): Boolean {
        return rolesRepository.findByUserId(userId).any {
            it.role!!.rolePriority() >= minimumRole.rolePriority()
        }
    }

    @Transactional(readOnly = true)
    fun getRoles(userId: Long): List<UserRole> {
        return rolesRepository.findByUserId(userId).map { it.role!! }
    }

    fun revokeRoles(userId: Long) {
        rolesRepository.deleteByUserId(userId)
    }

    fun revokeAllRoles() {
        rolesRepository.deleteAll()
    }

    fun setRoles(userId: Long, vararg roles: UserRole) {

        revokeRoles(userId)

        Arrays.stream(roles)
                .map { role -> UserRoleRecord(userId, role) }
                .forEach({ rolesRepository.save(it) })
    }
}
