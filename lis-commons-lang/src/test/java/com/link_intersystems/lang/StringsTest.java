package com.link_intersystems.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class StringsTest {

    @Test
    void isBlank() {
        assertTrue(Strings.isBlank(""), "empty string");
        assertTrue(Strings.isBlank("  "), "spaces string");
        assertTrue(Strings.isBlank(" \n\t"), "new lines and tabs string");

        assertFalse(Strings.isBlank(" \ne\t"), "new lines and tabs string");
    }
}