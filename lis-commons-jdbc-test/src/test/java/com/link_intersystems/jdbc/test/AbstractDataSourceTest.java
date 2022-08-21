package com.link_intersystems.jdbc.test;

import com.link_intersystems.jdbc.test.db.AbstractDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class AbstractDataSourceTest {

    private static interface SomeInterface {

    }

    private static class TestDataSource extends AbstractDataSource implements SomeInterface {

        @Override
        public Connection getConnection() throws SQLException {
            return null;
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return null;
        }
    }

    private AbstractDataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new TestDataSource();
    }

    @Test
    void unwrap() throws SQLException {
        assertSame(dataSource, dataSource.unwrap(SomeInterface.class));
        assertSame(dataSource, dataSource.unwrap(TestDataSource.class));

        assertNull(dataSource.unwrap(CharSequence.class));
    }

    @Test
    void isWrapperFor() throws SQLException {
        assertTrue(dataSource.isWrapperFor(SomeInterface.class));
        assertTrue(dataSource.isWrapperFor(TestDataSource.class));

        assertFalse(dataSource.isWrapperFor(CharSequence.class));
    }

    @Test
    void logWriter() throws SQLException {
        assertNull(dataSource.getLogWriter());

        PrintWriter pw = new PrintWriter(System.out);
        dataSource.setLogWriter(pw);

        assertSame(pw, dataSource.getLogWriter());
    }

    @Test
    void loginTimeout() throws SQLException {
        assertEquals(0, dataSource.getLoginTimeout());

        dataSource.setLoginTimeout(1);

        assertEquals(1, dataSource.getLoginTimeout());
    }

    @Test
    void getParentLogger() throws SQLFeatureNotSupportedException {
        assertNull(dataSource.getParentLogger());
    }
}