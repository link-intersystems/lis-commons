package com.link_intersystems.test.db;

import com.link_intersystems.sql.io.SqlScript;
import com.link_intersystems.test.jdbc.H2Database;
import com.link_intersystems.test.jdbc.H2JdbcUrl;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2DatabaseFactory {
    private DBSetup dbSetup;

    public H2DatabaseFactory(DBSetup dbSetup) {
        this.dbSetup = dbSetup;
    }

    public H2Database create() throws SQLException {
        return create("test");
    }

    public H2Database create(String databaseName) throws SQLException {
        H2JdbcUrl jdbcUrl = new H2JdbcUrl.Builder().setDatabaseName(databaseName).build();
        H2Database h2Database = new H2Database(jdbcUrl);

        Connection connection = h2Database.getConnection();

        dbSetup.setupSchema(connection);

        String schema = dbSetup.getSchema();
        h2Database.setSchema(schema);

        dbSetup.setupDdl(connection);

        dbSetup.setupData(connection);

        return h2Database;
    }
}
