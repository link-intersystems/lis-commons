package com.link_intersystems.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArgumentResolveExceptionTest {

    @Test
    void defaultConstructor() {
        ArgumentResolveException exception = new ArgumentResolveException();

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void withMessage() {
        ArgumentResolveException exception = new ArgumentResolveException("message");

        assertEquals("message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void withMessageAndCause() {
        Exception cause = new Exception();
        ArgumentResolveException exception = new ArgumentResolveException("message", cause);

        assertEquals("message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void withCause() {
        Exception cause = new Exception();
        ArgumentResolveException exception = new ArgumentResolveException(cause);

        assertEquals("java.lang.Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}