package com.link_intersystems.jdbc.test.db;

import com.link_intersystems.jdbc.test.H2Database;

import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface H2Factory {

    default H2Database create() throws SQLException {
        return create("test");
    }

    H2Database create(String databaseName) throws SQLException;
}
