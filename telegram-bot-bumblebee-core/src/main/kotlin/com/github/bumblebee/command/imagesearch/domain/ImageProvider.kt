package com.github.bumblebee.command.imagesearch.domain

interface ImageProvider {
    fun search(query: String): List<Image>
    fun name(): String = javaClass.simpleName
}
