package com.link_intersystems.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanInstantiationExceptionTest {

    @Test
    void withMessage() {
        BeanInstantiationException exception = new BeanInstantiationException("a message");

        assertEquals("a message", exception.getMessage());
    }

    @Test
    void withMessageAndCause() {
        Exception cause = new Exception();
        BeanInstantiationException exception = new BeanInstantiationException("a message", cause);

        assertEquals("a message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}