package com.link_intersystems.jdbc.format;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class DateLiteralFormatTest {

    private DateLiteralFormat literalFormat;

    @BeforeEach
    void setUp() {
        literalFormat = new DateLiteralFormat();
    }

    @Test
    void formatDate() throws Exception {
        Date date = new Date(122, 4, 21);

        String formatted = literalFormat.format(date);

        assertEquals("'2022-05-21'", formatted);
    }
}