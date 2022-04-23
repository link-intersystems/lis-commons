package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class PredicatesTest {

    @Test
    void firstPredicate() {
        Predicate<Object> firstPredicate = Predicates.firstPredicate();

        assertTrue(firstPredicate.test(""));
        assertFalse(firstPredicate.test(""));
    }

    @Test
    void equalDefault() {
        Predicate<String> stringPredicate = Predicates.equal("SomeString");

        assertTrue(stringPredicate.test(new String("SomeString")));
        assertTrue(stringPredicate.test("SomeString"));

        assertFalse(stringPredicate.test("someString"));
    }

    @Test
    void equalIgnoreCase() {
        Predicate<String> stringPredicate = Predicates.equal(String::equalsIgnoreCase, "SomeString");

        assertTrue(stringPredicate.test(new String("SomeString")));
        assertTrue(stringPredicate.test("SomeString"));
        assertTrue(stringPredicate.test("somestring"));
    }
}