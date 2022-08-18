package com.link_intersystems.jdbc;

import com.link_intersystems.jdbc.test.db.sakila.SakilaTinyTestDBExtension;
import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(SakilaTinyTestDBExtension.class)
@UnitTest
class ResultSetMapTest {
    private ScopedDatabaseMetaData scopedDatabaseMetaData;
    private Statement statement;
    private ResultSetMap resultSetMap;

    @BeforeEach
    void setUp(Connection connection) throws SQLException {
        scopedDatabaseMetaData = new ScopedDatabaseMetaData(connection.getMetaData(), null, "sakila");

        statement = connection.createStatement();
        statement.execute("select * from film where film_id = 1");
        ResultSet resultSet = statement.getResultSet();
        assertTrue(resultSet.next());

        resultSetMap = new ResultSetMap(resultSet);
    }

    @AfterEach
    void tearDown() throws SQLException {
        statement.close();
    }

    @Test
    void shortValue() {
        assertEquals((short) 1, resultSetMap.get("film_id"));
    }


    @Test
    void bigDecimalValue() {
        assertEquals(new BigDecimal("0.99"), resultSetMap.get("rental_rate"));
    }

    @Test
    void clobValue() {
        assertEquals("A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies", resultSetMap.get("description"));
    }

    @Test
    void varcharValue() {
        assertEquals("ACADEMY DINOSAUR", resultSetMap.get("title"));
    }

    @Test
    void dateValue() {
        assertEquals(LocalDate.of(2006, 1, 1), resultSetMap.get("release_year"));
    }

    @Test
    void dateTimeValue() {
        assertEquals(LocalDateTime.of(2006, 2, 15, 5, 3, 42), resultSetMap.get("last_update"));
    }
}