package com.link_intersystems.lang.ref;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMutableReferenceTest  {

    @Test
    void setAndGet() {
        DefaultMutableReference<String> defaultMutableReference = new DefaultMutableReference<String>();

        assertNull(defaultMutableReference.get());

        String newReferent = new String("someString");

        defaultMutableReference.set(newReferent);
        assertEquals(newReferent, defaultMutableReference.get());

        assertSame(newReferent, defaultMutableReference.get());
    }

    @Test
    void setViaConstructor() {
        String newReferent = new String("someString");

        DefaultMutableReference<String> defaultMutableReference = new DefaultMutableReference<String>(newReferent);

        assertEquals(newReferent, defaultMutableReference.get());
        assertSame(newReferent, defaultMutableReference.get());
    }
}
