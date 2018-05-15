package com.github.bumblebee.security

import com.github.telegram.domain.Update

interface UnauthorizedRequestAware {

    /**
     * This method will be called by security framework for [PrivilegedCommand]s
     * when user doesn't have privilege to execute certain command
     */
    fun onUnauthorizedRequest(update: Update)
}
