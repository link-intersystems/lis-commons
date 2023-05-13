package com.link_intersystems.util.text;

import org.junit.jupiter.api.Test;

import static com.link_intersystems.util.text.StandardTextAlignments.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class StandardTextAlignmentsTest {

    @Test
    void left() {
        assertEquals("hello       ", LEFT.align("hello", 12));
    }

    @Test
    void center() {
        assertEquals("    hello   ", CENTER.align("hello", 12));
    }

    @Test
    void right() {
        assertEquals("       hello", RIGHT.align("hello", 12));
    }
}