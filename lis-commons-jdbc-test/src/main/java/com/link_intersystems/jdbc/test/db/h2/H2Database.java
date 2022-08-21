package com.link_intersystems.jdbc.test.db.h2;

import com.link_intersystems.jdbc.test.db.AbstractDataSource;
import org.h2.engine.Mode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2Database extends AbstractDataSource implements AutoCloseable {

    public static interface StatementCallback {
        public void doWithStatement(Statement statement) throws SQLException;
    }

    public static interface StatementWithResultCallback<T> {
        public T doWithStatement(Statement statement) throws SQLException;
    }

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

    public static void setMode(Connection connection, Mode.ModeEnum mode) throws SQLException {
        String cmd = "SET MODE " + mode.name();
        try (Statement statement = connection.createStatement()) {
            statement.execute(cmd);
        }
    }


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

    private Collection<Connection> activeConnections = new ArrayList<>();

    private H2JdbcUrl h2JdbcUrl;
    private Connection realConnection;
    private Connection connectionProxy;

    private Boolean referentialIntegrity;
    private Mode.ModeEnum mode;

    private String schema;
    private String activeSchema;
    private String username;
    private String password;

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
        if (schema == null) {
            h2JdbcUrl.getSchema();
        }
        return schema;
    }

    public void setSchema(String schema) throws SQLException {
        this.schema = schema;
        updateConnection();
    }

    public void close() throws SQLException {


        if (!activeConnections.isEmpty()) {
            SQLException sqlException = null;

            for (Connection activeConnection : activeConnections) {
                try {
                    activeConnection.close();
                } catch (SQLException e) {
                    sqlException = sqlException == null ? e : chain(e, sqlException);
                }
            }

            if (sqlException != null) {
                throw sqlException;
            }

            realConnection = null;
            connectionProxy = null;
        }
    }

    private SQLException chain(SQLException e, SQLException next) {
        e.setNextException(next);
        return e;
    }

    /**
     * Removes all data, tables and schemas from the database.
     *
     * @throws SQLException
     */
    public void clear() throws SQLException {
        executeStatement("DROP ALL OBJECTS");
    }

    public void executeStatement(String sql) throws SQLException {
        executeStatement(s -> s.execute(sql));
    }

    public void executeStatement(StatementCallback statementCallback) throws SQLException {
        Connection connection = getRealConnection();
        try (Statement statement = connection.createStatement()) {
            statementCallback.doWithStatement(statement);
        }
    }

    public <T> T executeStatementWithResult(StatementWithResultCallback<T> statementCallback) throws SQLException {
        Connection connection = getRealConnection();
        try (Statement statement = connection.createStatement()) {
            return statementCallback.doWithStatement(statement);
        }
    }

    public void createSchema(String schema) throws SQLException {
        executeStatement("CREATE SCHEMA IF NOT EXISTS " + schema);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        if (username != null) {
            return username;
        }
        return h2JdbcUrl.getUsername();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        if (password != null) {
            return password;
        }
        return h2JdbcUrl.getPassword();
    }

    public H2JdbcUrl getJdbcUrl() {
        return h2JdbcUrl;
    }

    public void setReferentialIntegrity(boolean referentialIntegrity) throws SQLException {
        this.referentialIntegrity = referentialIntegrity;
        Connection connection = getRealConnection();
        setReferentialIntegrity(connection, referentialIntegrity);
    }

    public void setMode(Mode.ModeEnum mode) throws SQLException {
        this.mode = mode;


        Mode.ModeEnum currentMode = getMode();
        if (currentMode == null) {
            currentMode = Mode.ModeEnum.REGULAR;
        }
        setMode(getRealConnection(), currentMode);
    }

    public Mode.ModeEnum getMode() {
        if (mode == null) {
            return h2JdbcUrl.getMode();
        }
        return mode;
    }

    private Connection getRealConnection() throws SQLException {
        if (realConnection == null) {
            H2JdbcUrl jdbcUrl = getJdbcUrl();
            realConnection = DriverManager.getConnection(jdbcUrl.toString(), getUsername(), getPassword());
            activeConnections.add(realConnection);
        }

        updateConnection();
        return realConnection;
    }

    private void updateConnection() throws SQLException {
        if (realConnection == null) {
            return;
        }

        if (!Objects.equals(activeSchema, schema)) {
            String effectiveSchema = schema;
            if (effectiveSchema == null) {
                effectiveSchema = h2JdbcUrl.getSchema();
            }
            H2Database.setConnectionSchema(realConnection, effectiveSchema);
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
        H2JdbcUrl jdbcUrl = getJdbcUrl();
        Connection connection = DriverManager.getConnection(jdbcUrl.toString(), username, password);
        if (referentialIntegrity != null) {
            setReferentialIntegrity(connection, referentialIntegrity);
        }
        if (mode != null) {
            setMode(connection, mode);
        }
        Connection connectionProxy = ReusableConnectionProxy.createProxy(connection);
        activeConnections.add(connection);
        return connectionProxy;
    }


}
