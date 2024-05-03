package com.link_intersystems.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class SingleConnectionDataSourceTest {

    private Connection mockConn;
    private SingleConnectionDataSource singleConnectionDataSource;

    @BeforeEach
    void setUp() {
        this.mockConn = mock(Connection.class);
        this.singleConnectionDataSource = new SingleConnectionDataSource(mockConn);
    }

    @Test
    void getConnection() throws Exception {
        assertSame(mockConn, singleConnectionDataSource.getConnection());
    }

    @Test
    void getConnectionWithUsernameAndPassword() throws Exception {
        assertSame(mockConn, singleConnectionDataSource.getConnection("username", "password"));
    }
}