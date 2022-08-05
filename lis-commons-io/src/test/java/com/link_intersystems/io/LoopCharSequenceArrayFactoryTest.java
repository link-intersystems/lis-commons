package com.link_intersystems.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class LoopCharSequenceArrayFactoryTest {

    private CharSequenceArrayFactory charSequenceUtil;

    @BeforeEach
    void setUp() {
        charSequenceUtil = DefaultCharSequenceArrayFactory.INSTANCE;
    }

    @Test
    void toArray() {
        char[] array = charSequenceUtil.toArray("Hello");

        assertEquals("Hello", new String(array));
    }

}