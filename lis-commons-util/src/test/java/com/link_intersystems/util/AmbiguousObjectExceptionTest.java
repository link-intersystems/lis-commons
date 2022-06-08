package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class AmbiguousObjectExceptionTest {

    @Test
    void getMessage() {
        AmbiguousObjectException aoe = new AmbiguousObjectException(Arrays.asList("A", "B"), "Which one?");

        assertEquals("Which one?: A, B", aoe.getMessage());
    }

    @Test
    void getCandidates() {
        AmbiguousObjectException aoe = new AmbiguousObjectException(Arrays.asList("A", "B"));

        assertEquals(Arrays.asList("A", "B"), aoe.getCandidates());
    }
}