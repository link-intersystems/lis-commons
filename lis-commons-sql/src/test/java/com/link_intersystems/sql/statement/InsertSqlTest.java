package com.link_intersystems.sql.statement;

import com.link_intersystems.sql.format.DefaultTableLiteralFormat;
import com.link_intersystems.sql.format.TimestampLiteralFormat;
import com.link_intersystems.sql.format.QuotedStringLiteralFormat;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class InsertSqlTest {

    @Test
    void toSqlString() throws Exception {
        InsertSql insertSql = new InsertSql("actor");

        insertSql.addColumn("id", 1L);
        insertSql.addColumn("first_name", "PENELOPE");
        insertSql.addColumn("last_name", "GUINESS");
        insertSql.addColumn("last_update", new Date(106, 1, 15, 4, 34, 33));

        DefaultTableLiteralFormat tableLiteralFormat = new DefaultTableLiteralFormat();
        tableLiteralFormat.addLiteralFormat("first_name", new QuotedStringLiteralFormat());
        tableLiteralFormat.addLiteralFormat("last_name", new QuotedStringLiteralFormat());
        tableLiteralFormat.addLiteralFormat("last_update", new TimestampLiteralFormat());


        String sql = insertSql.toSqlString(tableLiteralFormat);
        assertEquals("insert into actor (id, first_name, last_name, last_update) values (1, 'PENELOPE', 'GUINESS', '2006-02-15 04:34:33')", sql);
    }
}