package com.link_intersystems.test.db;

import com.link_intersystems.test.jdbc.H2Database;

import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface H2DatabaseFactory {

    default H2Database create() throws SQLException {
        return create("test");
    }

    H2Database create(String databaseName) throws SQLException;
}
