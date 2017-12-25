package com.github.bumblebee.command.admin

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.security.UserRole
import com.github.bumblebee.security.roles.service.UserRolesService
import com.github.bumblebee.service.RandomPhraseService
import com.github.bumblebee.util.loggerFor
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import com.google.api.client.repackaged.com.google.common.base.Joiner
import org.springframework.stereotype.Component
import java.util.*
import java.util.regex.Pattern

/**
 * Command to manage user roles.
 */
@Component
class RoleAdminCommand(private val botApi: BotApi,
                       private val rolesService: UserRolesService,
                       private val randomPhraseService: RandomPhraseService) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhraseService.no())
            return
        }

        val currentUserId = update.message!!.from!!.id

        if (INIT_COMMAND == argument) {
            initializeRolesIfNeeded(currentUserId)
            return
        }

        if (ME_COMMAND == argument) {
            val roles = rolesService.getRoles(currentUserId)

            val reply = String.format("User id: %d, roles: %s", currentUserId,
                    if (roles.isEmpty()) "empty" else Joiner.on(", ").join(roles))
            botApi.sendMessage(chatId, reply)
            return
        }

        // check privilege
        if (!rolesService.hasPrivilege(currentUserId, UserRole.ADMIN)) {
            botApi.sendMessage(chatId, "You don't have privilege to assign roles.")
            return
        }

        invokeCommand(argument, currentUserId, chatId)
    }

    private fun invokeCommand(argument: String, currentUserId: Long?, chatId: Long?) {

        // set role
        var matcher = Pattern.compile(SET_COMMAND).matcher(argument)
        if (matcher.matches()) {
            val id = java.lang.Long.valueOf(matcher.group(1))
            val role = UserRole.valueOf(matcher.group(2).toUpperCase())

            if (hasPrivilegeToAssign(currentUserId, role)) {
                log.info("Setting role {} to user with id {}", role, id)
                rolesService.setRoles(id, role)
                botApi.sendMessage(chatId!!, "Roles had been set.")
            } else {
                deny(chatId)
            }

            return
        }

        // revoke roles from user
        matcher = Pattern.compile(REVOKE_COMMAND).matcher(argument)
        if (matcher.matches()) {
            val id = java.lang.Long.valueOf(matcher.group(1))

            val existingRole = rolesService.getRoles(id).stream()
                    .max(Comparator.comparingInt({ it.rolePriority() }))
                    .orElse(UserRole.USER)

            if (hasPrivilegeToAssign(currentUserId, existingRole)) {
                log.info("Revoking all roles from user with id {}", id)
                rolesService.revokeRoles(id)
                botApi.sendMessage(chatId!!, "Roles revoked.")
            } else {
                deny(chatId)
            }

            return
        }

        // drop all roles
        if (REVOKE_ALL == argument && rolesService.hasPrivilege(currentUserId!!, UserRole.SYSTEM)) {

            log.info("Revoking all roles")
            rolesService.revokeAllRoles()
            initializeRolesIfNeeded(currentUserId)

            botApi.sendMessage(chatId!!, "Roles has been reset.")

            return
        }

        // list roles
        if (LIST_COMMAND == argument) {
            val sb = StringBuilder()
            for (record in rolesService.getAllRoles()) {
                sb.append(record.userId)
                sb.append(": ")
                sb.append(record.role)
                sb.append("\n")
            }
            botApi.sendMessage(chatId!!, sb.toString())

            return
        }

        botApi.sendMessage(chatId!!, "I don't think that I got what you want...")
    }

    private fun deny(chatId: Long?) {
        botApi.sendMessage(chatId!!, "You cannot give higher permission than you already have.")
    }

    private fun hasPrivilegeToAssign(userId: Long?, role: UserRole): Boolean {
        return rolesService.getRoles(userId!!).stream()
                .anyMatch { currentRole -> currentRole.rolePriority() >= role.rolePriority() }
    }

    private fun initializeRolesIfNeeded(currentUserId: Long) {
        // if there is no roles in repository then
        // give all privileges to current user
        if (!rolesService.isRolesInitialized()) {
            rolesService.setRoles(currentUserId, UserRole.SYSTEM)
        }
    }

    companion object {

        private val log = loggerFor<RoleAdminCommand>()

        private val INIT_COMMAND = "init"
        private val SET_COMMAND = "set ([0-9]+) (" + allRoles() + ")"
        private val REVOKE_COMMAND = "revoke ([0-9]+)"
        private val REVOKE_ALL = "revoke all"
        private val LIST_COMMAND = "list"
        private val ME_COMMAND = "me"

        private fun allRoles(): String {
            return Joiner.on("|").join(UserRole.values())
        }
    }
}
