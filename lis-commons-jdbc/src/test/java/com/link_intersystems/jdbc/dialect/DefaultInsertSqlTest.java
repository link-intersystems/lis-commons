package com.link_intersystems.jdbc.dialect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class DefaultInsertSqlTest {

    @Test
    void toSqlString() {
        DefaultInsertSql actor = new DefaultInsertSql("actor");
        actor.addColumn("id", "1");
        actor.addColumn("first_name", "'PENELOPE'");
        actor.addColumn("last_name", "'GUINESS'");

        String sql = actor.toSqlString();

        assertEquals("insert into actor (id, first_name, last_name) values (1, 'PENELOPE', 'GUINESS')", sql);
    }
}