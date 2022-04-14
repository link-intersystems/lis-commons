package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class TransformedPredicateTest {

    @Test
    void test() {
        Transformer<Integer, String> transformer = (i) -> i.toString();
        Predicate<String> stringPredicate = (s) -> "2".equals(s);

        Predicate<Integer> predicate = new TransformedPredicate<>(transformer, stringPredicate);

        assertFalse(predicate.test(1));
        assertTrue(predicate.test(2));
    }
}