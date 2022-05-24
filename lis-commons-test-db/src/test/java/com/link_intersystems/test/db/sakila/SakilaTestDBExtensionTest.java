package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(SakilaSlimTestDBExtension.class)
@UnitTest
public class SakilaTestDBExtensionTest {

    private Connection connection;
    private Statement statement;

    @BeforeEach
    void setUp(Connection connection) throws SQLException {
        this.connection = connection;
        statement = connection.createStatement();
    }

    @AfterEach
    void tearDown() throws SQLException {
        statement.close();
    }

    @Test
    void languageCount() throws SQLException {
        statement.execute("SELECT count(*) FROM language");
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        int count = resultSet.getInt(1);
        assertEquals(6, count);
    }

    @Test
    void actorTest() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            if (stmt.execute("select * from actor where actor_id = 1")) {
                ResultSet actorResultSet = stmt.getResultSet();

                assertTrue(actorResultSet.next(), "result set should not be empty");

                assertEquals(1L, actorResultSet.getLong("actor_id"), "actor_id");
                assertEquals("PENELOPE", actorResultSet.getString("first_name"), "first_name");
                assertEquals("GUINESS", actorResultSet.getString("last_name"), "last_name");
            }
        }
    }

}

