package com.github.bumblebee.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomPhraseService {

    private static final String[] SURPRISE_PHRASES = {"Wut?", "Whaaat?"};
    private static final String[] NO_PHRASES = {"No", "Mmmm... No", "NO WAY", "Not today"};

    private Random random = new Random();

    public String surprise() {
        return SURPRISE_PHRASES[random.nextInt(SURPRISE_PHRASES.length)];
    }

    public String no() {
        return NO_PHRASES[random.nextInt(NO_PHRASES.length)];
    }
}
