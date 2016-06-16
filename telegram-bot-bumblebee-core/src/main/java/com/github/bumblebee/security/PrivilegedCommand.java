package com.github.bumblebee.security;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.bumblebee.security.UserRole.ADMIN;

/**
 * Define minimal role to execute annotated command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface PrivilegedCommand {

    /**
     * Name of command (can be used to bind command on alias)
     */
    String name();

    UserRole role() default ADMIN;
}
