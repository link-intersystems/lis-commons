package com.link_intersystems.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BeanClassExceptionTest {

    @Test
    void messageAndCause(){
        RuntimeException runtimeException = new RuntimeException();

        BeanClassException beanClassException = new BeanClassException("Message", runtimeException);

        assertEquals("Message", beanClassException.getMessage());
        assertSame(runtimeException, beanClassException.getCause());
    }
}