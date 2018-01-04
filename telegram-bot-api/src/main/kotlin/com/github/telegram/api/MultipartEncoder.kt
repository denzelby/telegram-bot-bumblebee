package com.github.telegram.api

import com.squareup.okhttp.MediaType
import com.squareup.okhttp.MultipartBuilder
import com.squareup.okhttp.RequestBody
import feign.RequestTemplate
import feign.codec.EncodeException
import feign.codec.Encoder
import okio.Buffer
import org.apache.commons.io.IOUtils
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * Poor mans support of multipart form data for Feign.
 */
class MultipartEncoder(private val delegate: Encoder) : Encoder {

    @Throws(EncodeException::class)
    override fun encode(obj: Any, bodyType: Type, template: RequestTemplate) {
        if (isMultipart(template)) {
            encodeAsMultipart(obj as Map<String, *>, bodyType, template)
        } else {
            delegate.encode(obj, bodyType, template)
        }
    }

    private fun isMultipart(template: RequestTemplate): Boolean {
        val contentType = template.headers()["Content-type"]
        return contentType != null && contentType.any { "multipart/form-data" == it }
    }

    private fun encodeAsMultipart(parts: Map<String, *>, bodyType: Type, template: RequestTemplate) {

        val builder = MultipartBuilder().type(MultipartBuilder.FORM)

        parts.forEach { partName, value ->
            if (value is InputFile) {
                addInputFilePart(builder, partName, value)
            } else {
                builder.addFormDataPart(partName, value.toString())
            }
        }

        val requestBody = builder.build()
        val buffer = Buffer()
        requestBody.writeTo(buffer)

        template.header("Content-type", requestBody.contentType().toString())
        template.body(buffer.readByteArray(), Charset.defaultCharset())
    }

    private fun addInputFilePart(builder: MultipartBuilder, partName: String, inputFile: InputFile) {
        inputFile.stream.use { stream ->
            builder.addFormDataPart(partName, inputFile.fileName,
                    RequestBody.create(MediaType.parse(inputFile.mimeType), IOUtils.toByteArray(stream))
            )
        }
    }

}
