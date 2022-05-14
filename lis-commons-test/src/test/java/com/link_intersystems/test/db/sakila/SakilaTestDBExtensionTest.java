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

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(SakilaTestDBExtension.class)
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

}

