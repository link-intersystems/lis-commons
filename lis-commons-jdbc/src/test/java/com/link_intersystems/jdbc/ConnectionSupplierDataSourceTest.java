package com.link_intersystems.jdbc;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ConnectionSupplierDataSourceTest {

    @Test
    void getConnection() throws SQLException {
        Connection connection = mock(Connection.class);

        ConnectionSupplierDataSource connectionSupplierDataSource = new ConnectionSupplierDataSource(() -> connection);

        assertSame(connection, connectionSupplierDataSource.getConnection());
        assertSame(connection, connectionSupplierDataSource.getConnection("", ""));
    }
}