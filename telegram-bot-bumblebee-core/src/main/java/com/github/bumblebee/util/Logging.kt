package com.github.bumblebee.util

import org.slf4j.LoggerFactory

inline fun <reified T : Any> logger(): org.slf4j.Logger = LoggerFactory.getLogger(T::class.java)