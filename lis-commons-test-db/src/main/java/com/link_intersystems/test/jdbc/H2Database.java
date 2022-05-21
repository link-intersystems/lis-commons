package com.link_intersystems.test.jdbc;

import java.sql.*;
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

    private H2JdbcUrl h2JdbcUrl;
    private H2JdbcUrl connectionUrl;
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
        reset();
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

    public void reset() throws SQLException {
        Connection connection = getRealConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP ALL OBJECTS");
        }
    }

    public boolean isIgnoreCase() {
        return h2JdbcUrl.isIgnoreCase();
    }

    public boolean isAutoCommit() {
        return h2JdbcUrl.isAutoCommit();
    }

    public String getInit() {
        return h2JdbcUrl.getInit();
    }

    public H2JdbcUrl getJdbcUrl() {
        return h2JdbcUrl;
    }

    public void setReferentialIntegrity(boolean referentialIntegrity) throws SQLException {
        String cmd = format("SET REFERENTIAL_INTEGRITY {0}", Boolean.toString(referentialIntegrity).toUpperCase());
        Connection connection = getRealConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(cmd);
        }
    }

    private Connection getRealConnection() throws SQLException {
        if (realConnection == null) {
            H2JdbcUrl jdbcUrl = getJdbcUrl();
            connectionUrl = jdbcUrl;
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


    public void doWithConnection(ConnectionCallback connectionCallback) throws SQLException {
        Connection connection = getConnection();
        connectionCallback.execute(connection);
    }

    public <T> T doWithConnection(ConnectionCallbackWithResult<T> connectionCallback) throws SQLException {
        Connection connection = getConnection();
        return connectionCallback.execute(connection);
    }


    public ResultSet execute(String sql, Object... args) throws SQLException {
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            if (ps.execute()) {
                return ps.getResultSet();
            }
            return null;
        }
    }


    public void executeScript(SqlScript sqlScript) throws SQLException {
        Connection connection = getRealConnection();
        sqlScript.execute(connection);

    }
}
