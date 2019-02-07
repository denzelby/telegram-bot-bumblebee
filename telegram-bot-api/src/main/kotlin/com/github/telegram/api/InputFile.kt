package com.github.telegram.api

import java.io.File
import java.io.InputStream

class InputFile(val mimeType: String, val fileName: String, val stream: InputStream) {

    companion object {
        private val MIME_PHOTO = "image/jpeg"
        private val MIME_AUDIO = "audio/mpeg"
        private val MIME_VIDEO = "video/mp4"
        private val MIME_VOICE = "audio/ogg"
        private val MIME_UNKNOWN = ""

        fun photo(file: File): InputFile = photo(file.inputStream(), file.name)
        fun audio(file: File): InputFile = audio(file.inputStream(), file.name)
        fun video(file: File): InputFile = video(file.inputStream(), file.name)
        fun voice(file: File): InputFile = voice(file.inputStream(), file.name)
        fun document(file: File): InputFile = document(file.inputStream(), file.name)

        fun photo(stream: InputStream, fileName: String): InputFile = InputFile(MIME_PHOTO, fileName, stream)
        fun audio(stream: InputStream, fileName: String): InputFile = InputFile(MIME_AUDIO, fileName, stream)
        fun video(stream: InputStream, fileName: String): InputFile = InputFile(MIME_VIDEO, fileName, stream)
        fun voice(stream: InputStream, fileName: String): InputFile = InputFile(MIME_VOICE, fileName, stream)
        fun document(stream: InputStream, fileName: String): InputFile = InputFile(MIME_UNKNOWN, fileName, stream)
    }
}
