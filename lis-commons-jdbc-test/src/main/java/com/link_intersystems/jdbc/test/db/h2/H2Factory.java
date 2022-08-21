package com.link_intersystems.jdbc.test.db.h2;

import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@FunctionalInterface
public interface H2Factory {
    H2Database create(String databaseName) throws SQLException;
}
