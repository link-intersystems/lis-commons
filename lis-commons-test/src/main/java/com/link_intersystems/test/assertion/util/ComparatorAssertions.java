package com.link_intersystems.test.assertion.util;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ComparatorAssertions<T> {

    private Comparator<T> comparator;

    public ComparatorAssertions(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public void assertGreater(T o1, T o2) {
        int compare = comparator.compare(o1, o2);
        assertTrue(compare > 0, () -> o1 + " should be greater than " + o2);
    }

    public void assertLower(T o1, T o2) {
        int compare = comparator.compare(o1, o2);
        assertTrue(compare < 0, () -> o1 + " should be lower than " + o2);
    }

    public void assertEqual(T o1, T o2) {
        int compare = comparator.compare(o1, o2);
        assertEquals(0, compare, () -> o1 + " should be equal to " + o2);
    }
}
