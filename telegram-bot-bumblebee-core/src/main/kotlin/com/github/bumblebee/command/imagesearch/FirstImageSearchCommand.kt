package com.github.bumblebee.command.imagesearch

import com.github.bumblebee.command.imagesearch.provider.google.GoogleCustomSearchProvider
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import org.springframework.stereotype.Component

@Component
class FirstImageSearchCommand(botApi: BotApi,
                              randomPhrase: RandomPhraseService,
                              googleProvider: GoogleCustomSearchProvider)
    : ImageSearchCommand(botApi, randomPhrase, listOf(googleProvider), { it }
)
