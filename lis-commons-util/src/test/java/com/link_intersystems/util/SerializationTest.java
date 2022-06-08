package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class SerializationTest {

    @Test
    void testClone() {
        String clone = Serialization.clone("ABC");

        assertNotSame(clone, "ABC");
        assertEquals(clone, "ABC");
    }

}