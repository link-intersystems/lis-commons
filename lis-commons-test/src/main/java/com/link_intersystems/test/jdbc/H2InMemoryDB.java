package com.link_intersystems.test.jdbc;

import java.io.Closeable;
import java.sql.*;
import java.util.Objects;

import static java.text.MessageFormat.format;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2InMemoryDB implements Closeable {

    private H2JdbcUrl h2JdbcUrl;
    private H2JdbcUrl connectionUrl;
    private Connection realConnection;
    private Connection connectionProxy;

    public H2InMemoryDB() {
        h2JdbcUrl = new H2JdbcUrl.Builder().build();
    }

    public String getDatabaseName() {
        return h2JdbcUrl.getDatabaseName();
    }

    public void setDatabaseName(String databaseName) {
        h2JdbcUrl = new H2JdbcUrl.Builder(h2JdbcUrl).setDatabaseName(databaseName).build();
    }

    public String getSchema() {
        return h2JdbcUrl.getSchema();
    }

    public void setSchema(String schema) {
        h2JdbcUrl = new H2JdbcUrl.Builder(h2JdbcUrl).setSchema(schema).build();
    }

    public void close() {
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

    public boolean isIgnoreCase() {
        return h2JdbcUrl.isIgnoreCase();
    }

    public void setIgnoreCase(boolean ignoreCase) {
        h2JdbcUrl = new H2JdbcUrl.Builder(h2JdbcUrl).setIgnoreCase(ignoreCase).build();
    }

    public boolean isAutoCommit() {
        return h2JdbcUrl.isAutoCommit();
    }

    public void setAutoCommit(boolean autoCommit) {
        h2JdbcUrl = new H2JdbcUrl.Builder(h2JdbcUrl).setAutoCommit(autoCommit).build();
    }

    public String getInit() {
        return h2JdbcUrl.getInit();
    }

    public void setInit(String init) {
        h2JdbcUrl = new H2JdbcUrl.Builder(h2JdbcUrl).setInit(init).build();
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
        if (realConnection != null && !Objects.equals(h2JdbcUrl, connectionUrl)) {
            realConnection.close();
            realConnection = null;
            connectionProxy = null;
        }

        if (realConnection == null) {
            H2JdbcUrl jdbcUrl = getJdbcUrl();
            connectionUrl = jdbcUrl;
            realConnection = DriverManager.getConnection(jdbcUrl.toString());
        }
        return realConnection;
    }

    public Connection getConnection() throws SQLException {
        if (connectionProxy != null && !Objects.equals(h2JdbcUrl, connectionUrl)) {
            connectionProxy = null;
        }

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
        try (Statement stmt = connection.createStatement()) {
            while (sqlScript.hasNext()) {
                stmt.execute(sqlScript.next());
            }
        }
    }
}
