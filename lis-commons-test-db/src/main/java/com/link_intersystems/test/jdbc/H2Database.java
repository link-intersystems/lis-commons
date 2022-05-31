package com.link_intersystems.test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.function.Predicate;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2Database implements AutoCloseable {

    public static final Predicate<String> SYSTEM_TABLE_PREDICATE = tableName -> asList(new String[]{
                    "constants",
                    "enum_values",
                    "in_doubt",
                    "index_columns",
                    "indexes",
                    "information_schema_catalog_name",
                    "locks",
                    "query_statistics",
                    "rights",
                    "roles",
                    "sessions",
                    "session_state",
                    "settings",
                    "synonyms",
                    "users",
            }
    ).contains(tableName);

    public static void setReferentialIntegrity(Connection connection, boolean referentialIntegrity) throws SQLException {
        String cmd = format("SET REFERENTIAL_INTEGRITY {0}", Boolean.toString(referentialIntegrity).toUpperCase());
        try (Statement statement = connection.createStatement()) {
            statement.execute(cmd);
        }
    }

    private H2JdbcUrl h2JdbcUrl;
    private Connection realConnection;
    private Connection connectionProxy;

    public H2Database() {
        this(new H2JdbcUrl.Builder().build());
    }

    public H2Database(H2JdbcUrl h2JdbcUrl) {
        this.h2JdbcUrl = Objects.requireNonNull(h2JdbcUrl);
    }

    public String getDatabaseName() {
        return h2JdbcUrl.getDatabaseName();
    }

    public String getSchema() {
        return h2JdbcUrl.getSchema();
    }

    public void close() throws SQLException {
        if (realConnection != null) {
            try {
                realConnection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            realConnection = null;
            connectionProxy = null;
        }
    }

    /**
     * Removes all data, tables and schemas from the database.
     *
     * @throws SQLException
     */
    public void clear() throws SQLException {
        Connection connection = getRealConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP ALL OBJECTS");
        }
    }

    public boolean isIgnoreCase() {
        return h2JdbcUrl.isIgnoreCase();
    }

    public H2JdbcUrl getJdbcUrl() {
        return h2JdbcUrl;
    }

    public void setReferentialIntegrity(boolean referentialIntegrity) throws SQLException {
        Connection connection = getRealConnection();
        setReferentialIntegrity(connection, referentialIntegrity);
    }

    private Connection getRealConnection() throws SQLException {
        if (realConnection == null) {
            H2JdbcUrl jdbcUrl = getJdbcUrl();
            realConnection = DriverManager.getConnection(jdbcUrl.toString());
        }
        return realConnection;
    }

    public Connection getConnection() throws SQLException {
        if (connectionProxy == null) {
            Connection realConnection = getRealConnection();
            connectionProxy = ReusableConnectionProxy.createProxy(realConnection);
        }

        return connectionProxy;
    }
}
