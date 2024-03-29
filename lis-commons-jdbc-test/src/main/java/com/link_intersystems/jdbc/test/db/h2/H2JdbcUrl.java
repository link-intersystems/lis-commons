package com.link_intersystems.jdbc.test.db.h2;

import org.h2.engine.Mode;

import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2JdbcUrl {


    public static class Builder {
        private String databaseName = "test";
        private String schema;
        private boolean ignoreCase = true;
        private boolean autoCommit;
        private String init;
        private boolean databaseToLower = true;
        private String databaseFilePath;
        private String username;
        private String password;
        private Mode.ModeEnum mode;

        public Builder() {

        }

        public Builder(H2JdbcUrl h2JdbcUrl) {
            databaseName = h2JdbcUrl.databaseName;
            schema = h2JdbcUrl.schema;
            ignoreCase = h2JdbcUrl.ignoreCase;
            autoCommit = h2JdbcUrl.autoCommit;
            init = h2JdbcUrl.init;
        }

        public Builder setDatabaseFilePath(String databaseFilePath) {
            this.databaseFilePath = databaseFilePath;
            return this;
        }

        public Builder setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Builder setSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public Builder setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
            return this;
        }

        public Builder setAutoCommit(boolean autoCommit) {
            this.autoCommit = autoCommit;
            return this;
        }

        public Builder setInit(String init) {
            this.init = init;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }


        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setMode(Mode.ModeEnum mode) {
            this.mode = mode;
            return this;
        }

        public H2JdbcUrl build() {
            return new H2JdbcUrl(
                    databaseFilePath,
                    databaseName,
                    schema,
                    ignoreCase,
                    autoCommit,
                    databaseToLower,
                    init,
                    username,
                    password, mode);
        }

    }

    private String databaseName;
    private String schema;
    private boolean ignoreCase;
    private boolean autoCommit;
    private String init;
    private String username;
    private String password;
    private boolean databaseToLower;
    private String databaseFilePath;
    private Mode.ModeEnum mode;


    private H2JdbcUrl(
            String databaseFilePath,
            String databaseName,
            String schema,
            boolean ignoreCase,
            boolean autoCommit,
            boolean databaseToLower,
            String init,
            String username,
            String password, Mode.ModeEnum mode) {
        this.databaseFilePath = databaseFilePath;
        this.databaseName = databaseName;
        this.schema = schema;
        this.ignoreCase = ignoreCase;
        this.autoCommit = autoCommit;
        this.databaseToLower = databaseToLower;
        this.init = init;
        this.username = username;
        this.password = password;
        this.mode = mode;
    }

    public Mode.ModeEnum getMode() {
        return mode;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getSchema() {
        return schema;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public String getInit() {
        return init;
    }

    public boolean isDatabaseToLower() {
        return databaseToLower;
    }

    public String getDatabaseFilePath() {
        return databaseFilePath;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        H2JdbcUrl h2JdbcUrl = (H2JdbcUrl) o;
        return ignoreCase == h2JdbcUrl.ignoreCase
                && autoCommit == h2JdbcUrl.autoCommit
                && Objects.equals(databaseName, h2JdbcUrl.databaseName)
                && databaseToLower == h2JdbcUrl.databaseToLower
                && Objects.equals(schema, h2JdbcUrl.schema)
                && Objects.equals(init, h2JdbcUrl.init);
    }

    @Override
    public int hashCode() {
        return Objects.hash(databaseName, schema, ignoreCase, autoCommit, databaseToLower, init);
    }

    @Override
    public String toString() {
        StringBuilder jdbcBuilder = new StringBuilder("jdbc:h2:");

        String databaseFilePath = getDatabaseFilePath();
        if (databaseFilePath == null) {
            jdbcBuilder.append("mem:");
        } else {
            jdbcBuilder.append("file:");
            jdbcBuilder.append(databaseFilePath);
            if (!databaseFilePath.endsWith("/")) {
                jdbcBuilder.append("/");
            }
        }

        if (databaseName != null) {
            jdbcBuilder.append(databaseName);
        }


        jdbcBuilder.append(";AUTOCOMMIT=");
        if (isAutoCommit()) {
            jdbcBuilder.append("ON");
        } else {
            jdbcBuilder.append("OFF");
        }

        if (isIgnoreCase()) {
            jdbcBuilder.append(";IGNORECASE=TRUE");
        }

        if (isDatabaseToLower()) {
            jdbcBuilder.append(";DATABASE_TO_LOWER=TRUE");
        }

        if (getSchema() != null) {
            jdbcBuilder.append(";SCHEMA=");
            jdbcBuilder.append(getSchema());
        }

        Mode.ModeEnum mode = getMode();
        if (mode != null) {
            jdbcBuilder.append(";MODE=");
            jdbcBuilder.append(mode.name().toUpperCase());
        }

        if (getUsername() != null) {
            jdbcBuilder.append(";USER=");
            jdbcBuilder.append(getUsername());
        }

        if (getPassword() != null) {
            jdbcBuilder.append(";PASSWORD=");
            jdbcBuilder.append(getPassword());
        }

        if (getInit() != null) {
            jdbcBuilder.append(";INIT=");
            jdbcBuilder.append(getInit());
        }

        return jdbcBuilder.toString();
    }
}
