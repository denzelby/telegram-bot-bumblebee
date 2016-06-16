package com.github.bumblebee.command.admin;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.security.UserRole;
import com.github.bumblebee.security.roles.domain.UserRoleRecord;
import com.github.bumblebee.security.roles.service.UserRolesService;
import com.github.bumblebee.service.RandomPhraseService;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command to manage user roles.
 */
@Component
public class RoleAdminCommand extends SingleArgumentCommand {

    private static final Logger log = LoggerFactory.getLogger(RoleAdminCommand.class);

    private static final String INIT_COMMAND = "init";
    private static final String SET_COMMAND = "set ([0-9]+) (" + allRoles() + ")";
    private static final String REVOKE_COMMAND = "revoke ([0-9]+)";
    private static final String REVOKE_ALL = "revoke all";
    private static final String LIST_COMMAND = "list";

    private final BotApi botApi;
    private final RandomPhraseService randomPhraseService;
    private final UserRolesService rolesService;

    @Autowired
    public RoleAdminCommand(BotApi botApi, UserRolesService rolesService, RandomPhraseService randomPhraseService) {
        this.botApi = botApi;
        this.rolesService = rolesService;
        this.randomPhraseService = randomPhraseService;
    }

    private static String allRoles() {
        return Joiner.on("|").join(UserRole.values());
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhraseService.no());
            return;
        }

        if (INIT_COMMAND.equals(argument)) {
            initializeRolesIfNeeded(update.getMessage().getFrom().getId());
            return;
        }

        Long currentUserId = update.getMessage().getFrom().getId();

        // check privilege
        if (!rolesService.hasPrivilege(currentUserId, UserRole.ADMIN)) {
            botApi.sendMessage(chatId, "You don't have privilege to assign roles.");
            return;
        }

        invokeCommand(argument, currentUserId, chatId);
    }

    private void invokeCommand(String argument, Long currentUserId, Long chatId) {

        // set role
        Matcher matcher = Pattern.compile(SET_COMMAND).matcher(argument);
        if (matcher.matches()) {
            Long id = Long.valueOf(matcher.group(1));
            UserRole role = UserRole.valueOf(matcher.group(2).toUpperCase());

            if (hasPrivilegeToAssign(currentUserId, role)) {
                log.info("Setting role {} to user with id {}", role, id);
                rolesService.setRoles(id, role);
                botApi.sendMessage(chatId, "Roles had been set.");
            } else {
                deny(chatId);
            }

            return;
        }

        // revoke roles from user
        matcher = Pattern.compile(REVOKE_COMMAND).matcher(argument);
        if (matcher.matches()) {
            Long id = Long.valueOf(matcher.group(1));

            UserRole existingRole = rolesService.getRoles(id).stream()
                    .max(Comparator.comparingInt(UserRole::rolePriority))
                    .orElse(UserRole.USER);

            if (hasPrivilegeToAssign(currentUserId, existingRole)) {
                log.info("Revoking all roles from user with id {}", id);
                rolesService.revokeRoles(id);
                botApi.sendMessage(chatId, "Roles revoked.");
            } else {
                deny(chatId);
            }

            return;
        }

        // drop all roles
        if (REVOKE_ALL.equals(argument) &&
                rolesService.hasPrivilege(currentUserId, UserRole.SYSTEM)) {

            log.info("Revoking all roles");
            rolesService.revokeAllRoles();
            initializeRolesIfNeeded(currentUserId);

            botApi.sendMessage(chatId, "Roles has been reset.");

            return;
        }

        // list roles
        if (LIST_COMMAND.equals(argument)) {
            StringBuilder sb = new StringBuilder();
            for (UserRoleRecord record : rolesService.getAllRoles()) {
                sb.append(record.getUserId());
                sb.append(": ");
                sb.append(record.getRole());
                sb.append("\n");
            }
            botApi.sendMessage(chatId, sb.toString());

            return;
        }

        botApi.sendMessage(chatId, "I don't think that I got what you want...");
    }

    private void deny(Long chatId) {
        botApi.sendMessage(chatId, "You cannot give higher permission than you already have.");
    }

    private boolean hasPrivilegeToAssign(Long userId, UserRole role) {
        return rolesService.getRoles(userId).stream()
                .anyMatch(currentRole -> currentRole.rolePriority() >= role.rolePriority());
    }

    private void initializeRolesIfNeeded(Long currentUserId) {
        // if there is no roles in repository then
        // give all privileges to current user
        if (!rolesService.isRolesInitialized()) {
            rolesService.setRoles(currentUserId, UserRole.SYSTEM);
        }
    }
}
