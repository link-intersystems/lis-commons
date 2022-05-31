package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.UnitTest;
import com.link_intersystems.test.db.AbstractTestDBExtension;
import com.link_intersystems.test.jdbc.DBAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

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

