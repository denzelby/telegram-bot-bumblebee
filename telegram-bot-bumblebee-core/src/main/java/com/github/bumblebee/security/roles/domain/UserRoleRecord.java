package com.github.bumblebee.security.roles.domain;

import com.github.bumblebee.security.UserRole;

import javax.persistence.*;

@Entity
@Table(name = "BB_USER_ROLES")
public class UserRoleRecord {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserRoleRecord() {
    }

    public UserRoleRecord(Long userId, UserRole role) {
        this.userId = userId;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
