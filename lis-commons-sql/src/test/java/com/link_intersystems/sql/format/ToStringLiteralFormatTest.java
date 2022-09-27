package com.link_intersystems.sql.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ToStringLiteralFormatTest {

    @Test
    void formatNull() throws Exception {
        ToStringLiteralFormat simpleLiteralFormat = new ToStringLiteralFormat();
        String formatted = simpleLiteralFormat.format(null);

        assertEquals(LiteralFormat.NULL_LITERAL, formatted);
    }

    @Test
    void formatValue() throws Exception {
        ToStringLiteralFormat simpleLiteralFormat = new ToStringLiteralFormat();
        String formatted = simpleLiteralFormat.format(34.12);

        assertEquals("34.12", formatted);
    }
}