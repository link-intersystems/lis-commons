package com.link_intersystems.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class SerializationExceptionTest {

    @Test
    void msgOnly() {
        SerializationException se = new SerializationException("test");

        assertEquals("test", se.getMessage());
        assertNull(se.getCause());
    }

    @Test
    void exceptionOnly() {
        RuntimeException re = new RuntimeException();
        SerializationException se = new SerializationException(re);

        assertNull(se.getMessage());
        assertEquals(re, se.getCause());
    }


}