package com.link_intersystems.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Predicate;

import static org.junit.Assert.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FilteredIteratorTest {

    private FilteredIterator<Character> filteredIterator;

    @Before
    public void setUp(){
        Iterator<Character> iterator = Arrays.asList('A', 'B', 'c', 'd', 'E').iterator();
        filteredIterator = new FilteredIterator<>(iterator, Character::isUpperCase);
    }

    @Test
    public void iterate() {
        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('A'),  filteredIterator.next());

        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('B'),  filteredIterator.next());

        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('E'),  filteredIterator.next());

        assertFalse(filteredIterator.hasNext());
    }

}