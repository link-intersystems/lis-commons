package com.link_intersystems.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ReversedListTest {

    private List<String> abc;
    private ReversedList<String> reversedValues;

    @BeforeEach
    void stUp() {
        abc = new ArrayList<>(asList("A", "B", "C"));
        reversedValues = new ReversedList<>(abc);
    }

    @Test
    void get() {
        assertEquals(asList("C", "B", "A"), new ReversedList<>(abc));
    }

    @Test
    void getOnSourceListChange() {
        abc.add("D");

        assertEquals(asList("D", "C", "B", "A"), reversedValues);
    }

    @Test
    void size() {
        assertEquals(3, reversedValues.size());
    }

    @Test
    void sizeOnSourceListChange() {
        abc.add("D");
        assertEquals(4, reversedValues.size());
    }
}