package com.github.bumblebee.service

import org.springframework.stereotype.Service
import java.util.*

@Service
class RandomPhraseService {

    private val random = Random()

    fun surprise(): String {
        return SURPRISE_PHRASES[random.nextInt(SURPRISE_PHRASES.size)]
    }

    fun no(): String {
        return NO_PHRASES[random.nextInt(NO_PHRASES.size)]
    }

    companion object {
        private val SURPRISE_PHRASES = arrayOf("Wut?", "Whaaat?")
        private val NO_PHRASES = arrayOf("No", "Mmmm... No", "NO WAY", "Not today")
    }
}
