package com.link_intersystems.assertion.util;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ComparatorAssertions {

    private Comparator comparator;

    public ComparatorAssertions(Comparator comparator) {
        this.comparator = comparator;
    }

    public void assertGreater(Object o1, Object o2) {
        int compare = comparator.compare(o1, o2);
        assertTrue(compare > 0, () -> o1 + " should be greater than " + o2);
    }

    public void assertLower(Object o1, Object o2) {
        int compare = comparator.compare(o1, o2);
        assertTrue(compare < 0, () -> o1 + " should be lower than " + o2);
    }

    public void assertEqual(Object o1, Object o2) {
        int compare = comparator.compare(o1, o2);
        assertEquals(0, compare, () -> o1 + " should be equal to " + o2);
    }
}
