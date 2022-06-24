package com.link_intersystems.jdbc.test.db.setup;

import com.link_intersystems.jdbc.test.db.H2DatabaseFactory;
import com.link_intersystems.jdbc.test.H2Database;
import com.link_intersystems.jdbc.test.H2JdbcUrl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DBSetupH2DatabaseFactory implements H2DatabaseFactory {

    private DBSetup dbSetup;
    private String schema;

    public DBSetupH2DatabaseFactory(DBSetup dbSetup) {
        this(dbSetup, null);
    }

    public DBSetupH2DatabaseFactory(DBSetup dbSetup, String schema) {
        this.dbSetup = Objects.requireNonNull(dbSetup);
        this.schema = schema;
    }

    @Override
    public H2Database create() throws SQLException {
        return create("test");
    }

    @Override
    public H2Database create(String databaseName) throws SQLException {
        H2JdbcUrl jdbcUrl = new H2JdbcUrl.Builder().setDatabaseName(databaseName).build();
        H2Database h2Database = new H2Database(jdbcUrl);

        try (Connection connection = h2Database.getConnection()) {
            dbSetup.setupSchema(connection);

            h2Database.setSchema(schema);

            dbSetup.setupDdl(connection);

            dbSetup.setupData(connection);
        }

        return h2Database;
    }
}
