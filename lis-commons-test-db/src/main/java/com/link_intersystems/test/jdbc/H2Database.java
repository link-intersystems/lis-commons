package com.link_intersystems.test.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2Database implements AutoCloseable, DataSource {

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

    public static final String DEFAULT_SCHEMA = "PUBLIC";


    public static void setReferentialIntegrity(Connection connection, boolean referentialIntegrity) throws SQLException {
        String cmd = format("SET REFERENTIAL_INTEGRITY {0}", Boolean.toString(referentialIntegrity).toUpperCase());
        try (Statement statement = connection.createStatement()) {
            statement.execute(cmd);
        }
    }

    public static void setConnectionSchema(Connection connection, String schema) throws SQLException {
        String cmd = format("SET SCHEMA {0}", schema == null ? DEFAULT_SCHEMA : schema);
        try (Statement statement = connection.createStatement()) {
            statement.execute(cmd);
        }
    }

    private H2JdbcUrl h2JdbcUrl;
    private Connection realConnection;
    private Connection connectionProxy;

    private String schema;
    private String activeSchema;

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

    public void setSchema(String schema) throws SQLException {
        this.schema = schema;
        updateConnection();
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
        executeStatement("DROP ALL OBJECTS");
    }

    protected void executeStatement(String sql) throws SQLException {
        Connection connection = getRealConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
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

        updateConnection();
        return realConnection;
    }

    private void updateConnection() throws SQLException {
        if (realConnection == null) {
            return;
        }

        if (!Objects.equals(activeSchema, schema)) {
            H2Database.setConnectionSchema(realConnection, schema);
            activeSchema = schema;
        }
    }

    public Connection getConnection() throws SQLException {
        if (connectionProxy == null) {
            Connection realConnection = getRealConnection();
            connectionProxy = ReusableConnectionProxy.createProxy(realConnection);
        }

        return connectionProxy;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (H2Database.class.equals(iface)) {
            return (T) this;
        }
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return H2Database.class.equals(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
