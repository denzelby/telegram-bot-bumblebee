package com.github.bumblebee.security;

public enum UserRole {

    SYSTEM(3), ADMIN(2), MODERATOR(1), USER(0), BANNED(-1);


    private final int rolePriority;

    UserRole(int rolePriority) {

        this.rolePriority = rolePriority;
    }

    public int rolePriority() {
        return rolePriority;
    }
}
