package com.link_intersystems.test.assertion.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ComparatorAssertionsTest {

    @Test
    void assertGreater() {
        ComparatorAssertions<Integer> integerComparatorAssertions = new ComparatorAssertions<>(Integer::compare);
        integerComparatorAssertions.assertGreater(2, 1);
        Assertions.assertThrows(AssertionFailedError.class, () -> integerComparatorAssertions.assertGreater(1, 2));
    }

    @Test
    void assertLower() {
        ComparatorAssertions<Integer> integerComparatorAssertions = new ComparatorAssertions<>(Integer::compare);
        integerComparatorAssertions.assertLower(1,2);
        Assertions.assertThrows(AssertionFailedError.class, () -> integerComparatorAssertions.assertLower(2, 1));
    }

    @Test
    void assertEqual() {
        ComparatorAssertions<Integer> integerComparatorAssertions = new ComparatorAssertions<>(Integer::compare);
        integerComparatorAssertions.assertEqual(1,1);
        Assertions.assertThrows(AssertionFailedError.class, () -> integerComparatorAssertions.assertEqual(2, 1));
    }
}