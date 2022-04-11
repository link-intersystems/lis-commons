package com.link_intersystems.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ChainedIteratorTest {


    @Before
    public void setUp() {

    }

    @Test
    public void iterate() {
        Iterator<String> iterator1 = asList("A", "B").iterator();
        Iterator<String> iterator2 = asList("C", "D").iterator();

        ChainedIterator<String> chainedIterator = new ChainedIterator<>(iterator1, iterator2);

        assertTrue(chainedIterator.hasNext());
        assertEquals("A", chainedIterator.next());
        assertTrue(chainedIterator.hasNext());
        assertEquals("B", chainedIterator.next());
        assertTrue(chainedIterator.hasNext());
        assertEquals("C", chainedIterator.next());
        assertTrue(chainedIterator.hasNext());
        assertEquals("D", chainedIterator.next());
        assertFalse(chainedIterator.hasNext());
    }

    @Test
    public void firstIteratorHasNoElements() {
        Iterator<String> iterator1 = Collections.emptyIterator();
        Iterator<String> iterator2 = asList("C", "D").iterator();

        ChainedIterator<String> chainedIterator = new ChainedIterator<>(iterator1, iterator2);

        assertTrue(chainedIterator.hasNext());
        assertEquals("C", chainedIterator.next());
        assertTrue(chainedIterator.hasNext());
        assertEquals("D", chainedIterator.next());
        assertFalse(chainedIterator.hasNext());
    }
}