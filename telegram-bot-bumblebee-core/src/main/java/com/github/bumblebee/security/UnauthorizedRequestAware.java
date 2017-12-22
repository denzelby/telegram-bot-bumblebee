package com.github.bumblebee.security;

import com.github.telegram.domain.Update;

public interface UnauthorizedRequestAware {

    /**
     * This method will be called by security framework for {@link PrivilegedCommand}s
     *  when user doesn't have privilege to execute certain command
     */
    void onUnauthorizedRequest(Update update);
}
