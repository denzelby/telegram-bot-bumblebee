package com.github.bumblebee.command.imagesearch.provider.google.response

import com.github.bumblebee.command.imagesearch.domain.Image

class GoogleSearchItem : Image {
    lateinit var link: String
    lateinit var mime: String

    override val url: String
        get() = link

    override val contentType: String
        get() = mime
}
