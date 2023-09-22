package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SequenceTest {

    @Test
    void stream() {
        Sequence<String> sequence = new ListSequence<>(Arrays.asList("a", "b"));

        List<String> elements = sequence.stream().collect(Collectors.toList());

        assertEquals(Arrays.asList("a", "b"), elements);
    }

    @Test
    void listIterator() {
        Sequence<String> sequence = new ListSequence<>(Arrays.asList("a", "b", "c"));

        ListIterator<String> listIterator = sequence.listIterator();

        assertTrue(listIterator.hasNext());
        assertFalse(listIterator.hasPrevious());
        assertEquals(0, listIterator.nextIndex());
        assertEquals(-1, listIterator.previousIndex());

        assertEquals("a", listIterator.next());
        assertEquals(1, listIterator.nextIndex());
        assertEquals(0, listIterator.previousIndex());
        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());


        assertEquals("b", listIterator.next());
        assertEquals(2, listIterator.nextIndex());
        assertEquals(1, listIterator.previousIndex());
        assertTrue(listIterator.hasNext());
        assertTrue(listIterator.hasPrevious());
        assertEquals("c", listIterator.next());
        assertEquals(3, listIterator.nextIndex());
        assertEquals(2, listIterator.previousIndex());

        assertTrue(listIterator.hasPrevious());
        assertFalse(listIterator.hasNext());

        assertThrows(NoSuchElementException.class, listIterator::next);
        assertThrows(NoSuchElementException.class, listIterator::next);
        assertEquals(3, listIterator.nextIndex());
        assertEquals(2, listIterator.previousIndex());

        assertEquals("c", listIterator.previous());
        assertEquals(2, listIterator.nextIndex());
        assertEquals(1, listIterator.previousIndex());
        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("b", listIterator.previous());
        assertEquals(1, listIterator.nextIndex());
        assertEquals(0, listIterator.previousIndex());
        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("a", listIterator.previous());
        assertEquals(0, listIterator.nextIndex());
        assertEquals(-1, listIterator.previousIndex());
        assertFalse(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertThrows(NoSuchElementException.class, listIterator::previous);
        assertThrows(NoSuchElementException.class, listIterator::previous);
        assertEquals(0, listIterator.nextIndex());
        assertEquals(-1, listIterator.previousIndex());

        listIterator.next();
        assertThrows(UnsupportedOperationException.class, listIterator::remove);
        assertThrows(UnsupportedOperationException.class, () -> listIterator.set(""));
        assertThrows(UnsupportedOperationException.class, () -> listIterator.add(""));

    }

    @Test
    void subSequence() {
        Sequence<String> sequence = new ListSequence<>(Arrays.asList("a", "b", "c", "d", "e"));


        Sequence<String> subSequence = sequence.subSequence(1, 4);

        assertEquals(Arrays.asList("b","c", "d"), subSequence.stream().collect(Collectors.toList()));

        assertThrows(IndexOutOfBoundsException.class, () -> subSequence.elementAt(3));

        assertThrows(IllegalArgumentException.class, () -> sequence.subSequence(3,2));
        assertThrows(IllegalArgumentException.class, () -> sequence.subSequence(2,6));
        System.out.println(assertThrows(IllegalArgumentException.class, () -> sequence.subSequence(-1,3)));

        Sequence<String> subSequenceIsEqualToOriginalSequence = sequence.subSequence(0, sequence.length());
        assertEquals(sequence.stream().collect(Collectors.toList()), subSequenceIsEqualToOriginalSequence.stream().collect(Collectors.toList()));
    }

    @Test
    void sequenceLength() {
        Sequence<String> sequence = new ListSequence<>(Arrays.asList("a", "b", "c", "d", "e"));
        assertEquals(5, sequence.length());
    }
}