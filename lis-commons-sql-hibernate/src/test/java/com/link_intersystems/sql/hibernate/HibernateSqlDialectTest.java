package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.dialect.DefaultSqlDialect;
import com.link_intersystems.sql.dialect.SqlDialect;
import com.link_intersystems.sql.format.LiteralFormat;
import com.link_intersystems.sql.statement.InsertSql;
import org.hibernate.dialect.H2Dialect;
import org.junit.jupiter.api.Test;

import java.sql.Types;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HibernateSqlDialectTest {

    @Test
    void createInsertSql() throws Exception {
        H2Dialect dialect = new H2Dialect();
        SqlDialect sqlDialect = new HibernateSqlDialect(dialect);

        InsertSql insertSql = sqlDialect.createInsertSql("actor");

        LiteralFormat idFormat = sqlDialect.getLiteralFormat(Types.BIGINT);
        insertSql.addColumn("id", idFormat.format(1L));

        LiteralFormat firstNameFormat = sqlDialect.getLiteralFormat(Types.VARCHAR);
        insertSql.addColumn("first_name", firstNameFormat.format("PENELOPE"));

        LiteralFormat lastNameFormat = sqlDialect.getLiteralFormat(Types.VARCHAR);
        insertSql.addColumn("last_name", lastNameFormat.format("GUINESS"));

        LiteralFormat lastUpdateFormat = sqlDialect.getLiteralFormat(Types.TIMESTAMP);
        insertSql.addColumn("last_update", lastUpdateFormat.format(new Date(106, 1, 15, 4, 34, 33)));

        String sql = insertSql.toSqlString();
        assertEquals("insert into actor (id, first_name, last_name, last_update) values (1, 'PENELOPE', 'GUINESS', '2006-02-15 04:34:33')", sql);
    }
}