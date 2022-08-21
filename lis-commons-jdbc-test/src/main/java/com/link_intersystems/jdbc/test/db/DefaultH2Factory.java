package com.link_intersystems.jdbc.test.db;

import com.link_intersystems.jdbc.test.H2Database;
import com.link_intersystems.jdbc.test.H2JdbcUrl;

import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultH2Factory implements H2Factory {

    @Override
    public H2Database create(String databaseName) throws SQLException {
        return new H2Database(new H2JdbcUrl.Builder().setDatabaseName(databaseName).build());
    }
}
