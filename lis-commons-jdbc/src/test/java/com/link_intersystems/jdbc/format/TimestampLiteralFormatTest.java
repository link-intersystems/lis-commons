package com.link_intersystems.jdbc.format;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class TimestampLiteralFormatTest {

    private TimestampLiteralFormat literalFormat;

    @BeforeEach
    void setUp() {
        literalFormat = new TimestampLiteralFormat();
    }

    @Test
    void formatDate() throws Exception {
        Date date = new Date(122, 4, 21, 12, 11, 34);

        String formatted = literalFormat.format(date);

        assertEquals("'2022-05-21 12:11:34'", formatted);
    }

    @Test
    void formatTimestamp() throws Exception {
        Timestamp date = new Timestamp(122, 4, 21, 12, 11, 34, 332);

        String formatted = literalFormat.format(date);

        assertEquals("'2022-05-21 12:11:34'", formatted);
    }
}