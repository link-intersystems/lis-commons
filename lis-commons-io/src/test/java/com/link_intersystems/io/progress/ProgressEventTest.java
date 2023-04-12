package com.link_intersystems.io.progress;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProgressEventTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void isDone() {
        assertFalse(new ProgressEvent(this, 0, 10, 9).isDone());
        assertTrue(new ProgressEvent(this, 0, 10, 10).isDone());
        assertTrue(new ProgressEvent(this, 0, 10, 11).isDone());
    }

    @Test
    void equalsAndHashCode() {
        assertEquals(new ProgressEvent(this, 0, 10, 9), new ProgressEvent(this, 0, 10, 9));
        assertEquals(new ProgressEvent(this, 0, 10, 9).hashCode(), new ProgressEvent(this, 0, 10, 9).hashCode());

        assertNotEquals(new ProgressEvent(this, 0, 10, 10), new ProgressEvent(this, 0, 10, 11));
    }

    @Test
    void toStringNoException() {
        assertNotNull(new ProgressEvent(this, 0, 10, 10).toString());
    }
}