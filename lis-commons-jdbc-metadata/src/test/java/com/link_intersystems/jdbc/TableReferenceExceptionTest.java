package com.link_intersystems.jdbc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class TableReferenceExceptionTest {

    @Test
    void message() {
        TableReferenceException exception = new TableReferenceException("test");

        assertEquals("test", exception.getMessage());
    }

    @Test
    void cause() {
        RuntimeException re = new RuntimeException();
        TableReferenceException exception = new TableReferenceException(re);

        assertSame(re, exception.getCause());
    }

    @Test
    void messageAndCause() {
        RuntimeException re = new RuntimeException();
        TableReferenceException exception = new TableReferenceException("test", re);

        assertSame(re, exception.getCause());
        assertEquals("test", exception.getMessage());
    }

}