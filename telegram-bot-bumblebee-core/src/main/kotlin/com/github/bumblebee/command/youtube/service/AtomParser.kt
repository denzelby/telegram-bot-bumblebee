package com.github.bumblebee.command.youtube.service

import com.github.bumblebee.command.youtube.entity.VideoNotification
import org.springframework.stereotype.Component
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory

@Component
class AtomParser {

    fun parse(xml: String): VideoNotification? {
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc = dBuilder.parse(InputSource(ByteArrayInputStream(xml.toByteArray())))
        doc.documentElement.normalize()

        val entry = doc.documentElement.getElementsByTagName("entry").item(0) as? Element
        val videoId = entry?.getElementsByTagName("yt:videoId")?.item(0)?.textContent
        val channelId = entry?.getElementsByTagName("yt:channelId")?.item(0)?.textContent

        return if (videoId != null && channelId != null)
            VideoNotification(videoId, channelId)
        else null
    }
}
