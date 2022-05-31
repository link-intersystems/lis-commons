package com.link_intersystems.test.jdbc;

import com.link_intersystems.test.db.sakila.SakilaSlimDB;
import com.link_intersystems.test.db.sakila.SakilaSlimTestDBExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(SakilaSlimTestDBExtension.class)
class H2DatabaseTest {

    private H2Database h2Database;
    private Connection connection;
    private SakilaSlimDB sakilaSlimDB;
    private DBAssertions dbAssertions;

    @BeforeEach
    void setUp(H2Database h2Database) throws SQLException {
        this.h2Database = h2Database;
        connection = h2Database.getConnection();
        dbAssertions = new DBAssertions(connection);
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