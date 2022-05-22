package com.link_intersystems.sql.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class SimpleLiteralFormatTest {

    @Test
    void formatNull() throws Exception {
        SimpleLiteralFormat simpleLiteralFormat = new SimpleLiteralFormat();
        String formatted = simpleLiteralFormat.format(null);

        assertEquals(LiteralFormat.NULL_LITERAL, formatted);
    }

    @Test
    void formatValue() throws Exception {
        SimpleLiteralFormat simpleLiteralFormat = new SimpleLiteralFormat();
        String formatted = simpleLiteralFormat.format(34.12);

        assertEquals("34.12", formatted);
    }
}