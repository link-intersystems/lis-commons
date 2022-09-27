package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.AbstractLiteralFormat;
import com.link_intersystems.sql.statement.ColumnValue;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HibernateTableLiteralFormatTest {

    private HibernateTableLiteralFormat literalFormat;

    @BeforeEach
    void setUp() {
        literalFormat = new HibernateTableLiteralFormat(new PostgreSQL9Dialect());
    }

    @Test
    void format() throws Exception {
        String formatted = literalFormat.format(new ColumnValue("amount", new BigDecimal("1234.45")));

        assertEquals("1234.45", formatted);
    }

    @Test
    void formatInt() throws Exception {
        String formatted = literalFormat.format(new ColumnValue("amount", 1));

        assertEquals("1", formatted);
    }


    @Test
    void formatNull() throws Exception {
        String formatted = literalFormat.format(new ColumnValue("amount", null));

        assertEquals("null", formatted);
    }

    @Test
    void defaultLiteralFormat() throws Exception {
        literalFormat.setDefaultLiteralFormat(new DoubleQuotedLiteralFormat());

        String formatted = literalFormat.format(new ColumnValue("amount", new BigDecimal("1234.45")));

        assertEquals("1234.45", formatted);
    }

    @Test
    void overrideLiteralFormat() throws Exception {
        literalFormat.setLiteralFormatOverride(Integer.class, new DoubleQuotedLiteralFormat());

        String formatted = literalFormat.format(new ColumnValue("amount", 1));

        assertEquals("\"1\"", formatted);
    }

    @Test
    void formatBlob() throws Exception {
        byte[] byteArray = {'a', 'b', 'c'};

        String formatted = literalFormat.format(new ColumnValue("value", byteArray));

        assertEquals("e1e2e3", formatted);
    }

    private static class DoubleQuotedLiteralFormat extends AbstractLiteralFormat {
        @Override
        protected String doFormat(Object value) {
            return "\"" + value + "\"";
        }
    }
}