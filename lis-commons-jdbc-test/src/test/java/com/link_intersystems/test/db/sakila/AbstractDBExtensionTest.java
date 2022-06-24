package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.UnitTest;
import com.link_intersystems.jdbc.test.DBAssertions;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@UnitTest
public abstract class AbstractDBExtensionTest {

    protected Connection connection;
    protected DBAssertions dbAssertions;

    @BeforeEach
    void setUp(Connection connection) throws SQLException {
        this.connection = connection;
        dbAssertions = new DBAssertions(connection);
    }

}

