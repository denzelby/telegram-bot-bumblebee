package com.github.bumblebee.command.youtube.service

import com.github.bumblebee.command.youtube.entity.VideoNotification
import org.junit.Assert.assertEquals
import org.junit.Test


class AtomParserTest {

    private val xml = """
        <?xml version='1.0' encoding='UTF-8'?>
        <feed xmlns="http://www.w3.org/2005/Atom" xmlns:yt="http://www.youtube.com/xml/schemas/2015">
            <link href="https://pubsubhubbub.appspot.com" rel="hub"/>
            <link href="https://www.youtube.com/xml/fee ds/videos.xml?channel_id=UCZRc1nFfH2FmGd2feIBlPOQ" rel="self"/>
            <title>YouTube video feed</title>
            <updated>2018-03-07T10:00:41.416229036+00:00</updated>
            <entry>
                <id>yt:video:1OYzEVHUfqs</id>
                <yt:videoId>1OYzEVHUfqs</yt:videoId>
                <yt:channelId>UCZRc1nFfH2FmGd2feIBlPOQ</yt:channelId>
                <title>Some title!</title>
                <link href="https://www.youtube.com/watch?v=1OYzEVHUfqs" rel="alternate"/>
                <author>
                    <name>Some author</name>
                    <uri>https://www.youtube.com/channel/UCZRc1nFfH2FmGd2feIBlPOQ</uri>
                </author>
                <published>2018-03-07T10:00:00+00:00</published>
                <updated>2018-03-07T10:00:41.416229036+00:00</updated>
            </entry>
        </feed>
        """.trimIndent()

    @Test
    fun parse() {
        // given
        val parser = AtomParser()

        // when
        val notification = parser.parse(xml)

        // then
        assertEquals(VideoNotification("1OYzEVHUfqs", "UCZRc1nFfH2FmGd2feIBlPOQ"), notification)
    }
}