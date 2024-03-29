package com.link_intersystems.jdbc.test;

import com.link_intersystems.jdbc.test.db.h2.H2Database;
import com.link_intersystems.jdbc.test.db.setup.DBSetupH2DatabaseFactory;
import com.link_intersystems.jdbc.test.db.sakila.SakilaSlimDB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class H2DatabaseSakilaTest {

    private H2Database h2Database;
    private Connection connection;
    private DBAssertions dbAssertions;

    @BeforeEach
    void setUp() throws SQLException {
        DBSetupH2DatabaseFactory dbSetupH2DatabaseFactory = new DBSetupH2DatabaseFactory(new SakilaSlimDB(), "sakila");
        h2Database = dbSetupH2DatabaseFactory.create("test");
        connection = h2Database.getConnection();
        dbAssertions = new DBAssertions(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        h2Database.close();
    }

    @Test
    void clear() throws SQLException {
        h2Database.clear();

        dbAssertions.assertTableNotExists("actor");
        dbAssertions.assertSchemaNotExists("sakila");
    }

    @Test
    void close() throws SQLException {
        h2Database.close();

        assertThrows(SQLException.class, () -> connection.createStatement());
    }

}