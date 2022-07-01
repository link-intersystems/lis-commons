package com.link_intersystems.jdbc.test;

import org.h2.engine.Mode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class H2DatabaseTest {

    private H2Database h2Database;

    @BeforeEach
    void setUp() {
        H2JdbcUrl h2JdbcUrl = new H2JdbcUrl.Builder()
                .setDatabaseName("testdb")
                .setUsername("test")
                .setPassword("pass1")
                .build();
        h2Database = new H2Database(h2JdbcUrl);
    }

    @AfterEach
    void tearDown() throws SQLException {
        h2Database.close();
    }

    @Test
    void setMode() throws SQLException {
        h2Database.setMode(Mode.ModeEnum.DB2);

        assertEquals(Mode.ModeEnum.DB2, h2Database.getMode());
    }

    @Test
    void setReferentialIntegrity() {
    }

    @Test
    void getDatabaseName() {
        String databaseName = h2Database.getDatabaseName();
        assertEquals("testdb", databaseName);
    }

    @Test
    void getSchema() throws SQLException {
        h2Database.createSchema("testschema");

        h2Database.setSchema("testschema");

        assertEquals("testschema", h2Database.getSchema());

        String result = h2Database.executeStatementWithResult(s -> {
            s.execute(" SELECT SCHEMA()");
            s.getResultSet().next();
            return s.getResultSet().getString(1);
        });
        assertEquals("testschema", result);
    }

    @Test
    void setUsername() {
    }

    @Test
    void setPassword() {
    }

    @Test
    void getConnection() throws SQLException {
        Connection connection = h2Database.getConnection("test", "pass1");
        assertNotNull(connection);
    }
}