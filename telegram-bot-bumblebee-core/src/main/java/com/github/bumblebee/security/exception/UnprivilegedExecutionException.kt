package com.github.bumblebee.security.exception

class UnprivilegedExecutionException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
