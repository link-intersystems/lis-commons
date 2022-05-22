package com.link_intersystems.sql.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class AbstractLiteralFormatTest {

    @Test
    void formatNull() throws Exception {
        AbstractLiteralFormat abstractLiteralFormat = new AbstractLiteralFormat() {
            @Override
            protected String doFormat(Object value) {
                throw new UnsupportedOperationException();
            }
        };

        String formatted = abstractLiteralFormat.format(null);

        assertEquals(LiteralFormat.NULL_LITERAL, formatted);
    }

    @Test
    void format() throws Exception {
        AbstractLiteralFormat abstractLiteralFormat = new AbstractLiteralFormat() {
            @Override
            protected String doFormat(Object value) {
                return String.valueOf(value);
            }
        };

        String formatted = abstractLiteralFormat.format("TEST");

        assertEquals("TEST", formatted);
    }
}