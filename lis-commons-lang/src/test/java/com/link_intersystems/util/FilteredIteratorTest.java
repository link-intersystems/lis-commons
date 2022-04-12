package com.link_intersystems.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FilteredIteratorTest {

    private FilteredIterator<Character> filteredIterator;

    @BeforeEach
    void setUp(){
        Iterator<Character> iterator = Arrays.asList('A', 'B', 'c', 'd', 'E').iterator();
        filteredIterator = new FilteredIterator<>(iterator, Character::isUpperCase);
    }

    @Test
    void iterate() {
        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('A'),  filteredIterator.next());

        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('B'),  filteredIterator.next());

        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('E'),  filteredIterator.next());

        assertFalse(filteredIterator.hasNext());
    }

}