package com.link_intersystems.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class TransformedIteratorTest {

    private TransformedIterator<Character, Character> transformedIterator;
    private ArrayList<Character> characters;

    @BeforeEach
    void setUp() {
        characters = new ArrayList<>(Arrays.asList('A', 'B'));

        transformedIterator = new TransformedIterator<>(characters.iterator(), Character::toLowerCase);
    }

    @Test
    void hasNext() {
        assertTrue(transformedIterator.hasNext());
        assertEquals('a', transformedIterator.next());
        assertTrue(transformedIterator.hasNext());
        assertEquals('b', transformedIterator.next());
        assertFalse(transformedIterator.hasNext());
    }

    @Test
    void remove(){
        transformedIterator.next();

        transformedIterator.remove();

        assertEquals(singletonList('B'), characters);
    }


}