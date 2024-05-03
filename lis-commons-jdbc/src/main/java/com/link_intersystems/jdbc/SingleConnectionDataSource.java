package com.link_intersystems.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

public class SingleConnectionDataSource extends AbstractDataSource {
    private final Connection connection;

    public SingleConnectionDataSource(Connection connection) {
        this.connection = requireNonNull(connection);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

}
