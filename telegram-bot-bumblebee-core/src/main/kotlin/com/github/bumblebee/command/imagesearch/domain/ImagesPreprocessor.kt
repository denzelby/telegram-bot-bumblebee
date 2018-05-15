package com.github.bumblebee.command.imagesearch.domain

interface ImagesPreprocessor {

    fun process(images: List<Image>)
}
