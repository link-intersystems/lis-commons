package com.link_intersystems.sql.hibernate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HibernateSqlFormatterTest {

    @Test
    void formatSql() {
        HibernateSqlFormatter sqlFormatter = new HibernateSqlFormatter();

        String formattedSql = sqlFormatter.format("insert into actor (id, first_name, last_name, last_update) values (1, 'PENELOPE', 'GUINESS', '2006-02-15 04:34:33')");

        String newLine = System.lineSeparator();

        assertEquals("insert " + newLine +
                "into" + newLine +
                "    actor" + newLine +
                "    (id, first_name, last_name, last_update) " + newLine +
                "values" + newLine +
                "    (1, 'PENELOPE', 'GUINESS', '2006-02-15 04:34:33')", formattedSql);
    }
}