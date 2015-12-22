package com.github.bumblebee.command.imagesearch;

import com.github.bumblebee.command.imagesearch.provider.bingpics.BingPictureSearchProvider;
import com.github.bumblebee.command.imagesearch.provider.googlepics.GoogleCustomSearchProvider;
import com.github.bumblebee.service.RandomPhraseService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;

import java.util.Collections;

@Component
public class RandomImageSearchCommand extends ImageSearchCommand {

    @Autowired
    public RandomImageSearchCommand(BotApi botApi, RandomPhraseService randomPhrase,
                                    GoogleCustomSearchProvider googleProvider,
                                    BingPictureSearchProvider bingProvider) {
        super(botApi, randomPhrase, Lists.newArrayList(googleProvider, bingProvider), Collections::shuffle);
    }
}
