package com.link_intersystems.sql.format;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class SqlFormatSettingsTest {

    private SqlFormatSettings sqlFormatSettings;

    @BeforeEach
    void setUp() {
        sqlFormatSettings = new SqlFormatSettings();
    }

    @Test
    void setStatementDelimiter() {
        sqlFormatSettings.setStatementDelimiter("");

        assertEquals("", sqlFormatSettings.getStatementDelimiter());
    }

    @Test
    void getStatementDelimiter() {
        assertEquals(";", sqlFormatSettings.getStatementDelimiter());
    }

    @Test
    void getSqlFormatter() {
        SqlFormatter sqlFormatter = sqlFormatSettings.getSqlFormatter();

        assertNotNull(sqlFormatter);

        String sql = "insert into actor (id, first_name, last_name) values (1, 'PENELOPE', 'GUINESS')";
        String formatted = sqlFormatter.format(sql);

        assertEquals(sql, formatted);
    }

    @Test
    void setSqlFormatter() {
        SqlFormatter formatter = s -> s;

        sqlFormatSettings.setSqlFormatter(formatter);

        SqlFormatter sqlFormatter = sqlFormatSettings.getSqlFormatter();

        assertSame(formatter, sqlFormatter);
    }

    @Test
    void getStatementSeparator() {
        assertEquals(System.lineSeparator(), sqlFormatSettings.getStatementSeparator());
    }

    @Test
    void setStatementSeparator() {
        sqlFormatSettings.setStatementSeparator("\n\n\n");

        assertEquals("\n\n\n", sqlFormatSettings.getStatementSeparator());
    }
}