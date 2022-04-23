package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class IteratorsTest {

    @Test
    void iterableToStream() {
        Iterable<String> iterable = asList("A", "B", "C");

        Stream<String> stream = Iterators.toStream(iterable);

        List<String> collectedStream = stream.collect(Collectors.toList());
        assertEquals(asList("A", "B", "C"), collectedStream);
    }

    @Test
    void iteratorToStream() {
        Iterator<String> iterator = asList("A", "B", "C").iterator();

        Stream<String> stream = Iterators.toStream(iterator);

        List<String> collectedStream = stream.collect(Collectors.toList());
        assertEquals(asList("A", "B", "C"), collectedStream);
    }

    @Test
    void iterableToList() {
        Iterable<String> iterable = asList("A", "B", "C");

        List<String> asList = Iterators.toList(iterable);

        assertEquals(asList("A", "B", "C"), asList);
    }

    @Test
    void iteratorToList() {
        Iterator<String> iterator = asList("A", "B", "C").iterator();

        List<String> asList = Iterators.toList(iterator);

        assertEquals(asList("A", "B", "C"), asList);
    }

    @Test
    void filteredIterator() {
        Iterator<Character> iterator = Arrays.asList('A', 'B', 'c', 'd', 'E').iterator();

        Iterator<Character> filteredIterator = Iterators.filtered(iterator, Character::isUpperCase);

        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('A'), filteredIterator.next());

        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('B'), filteredIterator.next());

        assertTrue(filteredIterator.hasNext());
        assertEquals(Character.valueOf('E'), filteredIterator.next());

        assertFalse(filteredIterator.hasNext());
    }

    @Test
    void chainedIterators() {
        Iterator<String> iterator1 = asList("A", "B").iterator();
        Iterator<String> iterator2 = asList("C", "D").iterator();

        Iterator<String> chainedIterator = Iterators.chained(iterator1, iterator2);

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
    void chainedIteratorsFirstOneHasNoElements() {
        Iterator<String> iterator1 = Collections.emptyIterator();
        Iterator<String> iterator2 = asList("C", "D").iterator();

        Iterator<String> chainedIterator = Iterators.chained(iterator1, iterator2);

        assertTrue(chainedIterator.hasNext());
        assertEquals("C", chainedIterator.next());
        assertTrue(chainedIterator.hasNext());
        assertEquals("D", chainedIterator.next());
        assertFalse(chainedIterator.hasNext());
    }

    @Test
    void transformedIterator() {
        Iterator<Character> iterator = asList('A', 'B', 'C').iterator();

        Iterator<? super Character> transformed = Iterators.transformed(iterator, Character::toLowerCase);

        assertTrue(transformed.hasNext());
        assertEquals('a', transformed.next());
        assertTrue(transformed.hasNext());
        assertEquals('b', transformed.next());
        assertTrue(transformed.hasNext());
        assertEquals('c', transformed.next());
        assertFalse(transformed.hasNext());
    }


}