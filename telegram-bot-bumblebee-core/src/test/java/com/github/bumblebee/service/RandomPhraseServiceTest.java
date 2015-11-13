package com.github.bumblebee.service;

import junit.framework.AssertionFailedError;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Supplier;

public class RandomPhraseServiceTest {

    private RandomPhraseService service = new RandomPhraseService();

    @Test
    public void testSurprise() {
        test(2, service::surprise);
    }

    @Test
    public void testNo() {
        test(4, service::no);
    }

    private void test(int expectedPhraseCount, Supplier<String> phraseSupplier) {

        final Set<String> results = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            results.add(phraseSupplier.get());
            if (results.size() == expectedPhraseCount) {
                return;
            }
        }

        StringJoiner joiner = new StringJoiner(", ");
        results.stream().forEach(joiner::add);
        throw new AssertionFailedError("Not all phrases were generated.\n" +
                "expected: " + expectedPhraseCount + "\n" +
                "actual: " + joiner.toString());
    }

}