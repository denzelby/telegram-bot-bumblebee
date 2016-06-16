package com.github.bumblebee.security.roles.service;

import com.github.bumblebee.security.UserRole;
import com.github.bumblebee.security.roles.dao.UserRolesRepository;
import com.github.bumblebee.security.roles.domain.UserRoleRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserRolesService {

    private final UserRolesRepository rolesRepository;
    private boolean rolesInitialized;

    @Autowired
    public UserRolesService(UserRolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Transactional(readOnly = true)
    public boolean hasPrivilege(long userId, UserRole minimumRole) {

        return rolesRepository.findByUserId(userId).stream()
                .anyMatch(rec -> rec.getRole().rolePriority() >= minimumRole.rolePriority());
    }

    @Transactional(readOnly = true)
    public List<UserRole> getRoles(long userId) {
        return rolesRepository.findByUserId(userId).stream()
                .map(UserRoleRecord::getRole)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<UserRoleRecord> getAllRoles() {
        return rolesRepository.findAll();
    }

    public void revokeRoles(long userId) {
        rolesRepository.deleteByUserId(userId);
    }

    public void revokeAllRoles() {
        rolesRepository.deleteAll();
    }

    public void setRoles(long userId, UserRole... roles) {

        revokeRoles(userId);

        Arrays.stream(roles)
                .map(role -> new UserRoleRecord(userId, role))
                .forEach(rolesRepository::save);
    }

    public boolean isRolesInitialized() {
        if (!rolesInitialized) {
            rolesInitialized = rolesRepository.count() > 0;
        }
        return rolesInitialized;
    }
}
