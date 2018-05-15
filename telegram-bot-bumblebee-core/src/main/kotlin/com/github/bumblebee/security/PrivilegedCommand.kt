package com.github.bumblebee.security

import com.github.bumblebee.security.UserRole.ADMIN
import org.springframework.stereotype.Component

/**
 * Define minimal role to execute annotated command.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Component
annotation class PrivilegedCommand(
        /**
         * Name of command (can be used to bind command on alias)
         */
        val name: String, val role: UserRole = ADMIN)
