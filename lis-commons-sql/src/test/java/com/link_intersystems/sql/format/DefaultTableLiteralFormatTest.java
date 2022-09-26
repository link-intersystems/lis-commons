package com.link_intersystems.sql.format;

import com.link_intersystems.sql.statement.ColumnValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class DefaultTableLiteralFormatTest {

    @Test
    void format() throws Exception {
        DefaultTableLiteralFormat tableLiteralFormat = new DefaultTableLiteralFormat();
        tableLiteralFormat.setDefaultLiteralFormat(new AbstractLiteralFormat() {
            @Override
            protected String doFormat(Object value) throws Exception {
                return "\"" + value + "\"";
            }
        });

        String formatted = tableLiteralFormat.format(new ColumnValue("col1", "value"));
        Assertions.assertEquals("\"value\"", formatted);
    }
}