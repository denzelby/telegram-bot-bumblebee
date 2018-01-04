package com.github.bumblebee.command.youtube.service

import com.github.bumblebee.command.youtube.entity.AtomFeed
import org.springframework.stereotype.Component
import java.io.StringReader
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.transform.stream.StreamSource

@Component
class AtomParser {

    @Throws(JAXBException::class)
    fun parse(input: String): AtomFeed {
        val context = JAXBContext.newInstance(AtomFeed::class.java)
        val unmarshaller = context.createUnmarshaller()
        val root = unmarshaller.unmarshal(StreamSource(StringReader(input)), AtomFeed::class.java)
        return root.value
    }
}
