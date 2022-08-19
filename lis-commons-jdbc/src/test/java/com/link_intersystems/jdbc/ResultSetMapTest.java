package com.link_intersystems.jdbc;

import com.link_intersystems.jdbc.test.db.H2DatabaseConfig;
import com.link_intersystems.jdbc.test.db.H2TestDBExtension;
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
import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(H2TestDBExtension.class)
@H2DatabaseConfig(databaseSetup = ResultSetMapDBSetup.class)
@UnitTest
class ResultSetMapTest {
    private Statement statement;
    private ResultSetMap resultSetMap;

    @BeforeEach
    void setUp(Connection connection) throws SQLException {

        statement = connection.createStatement();
        statement.execute("select * from resultsetmap where id = 1");
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
        assertEquals(Short.MAX_VALUE, resultSetMap.get("shortvalue"));
    }


    @Test
    void intValue() {
        assertEquals(Integer.MAX_VALUE, resultSetMap.get("intvalue"));
    }

    @Test
    void longValue() {
        assertEquals(Long.MAX_VALUE, resultSetMap.get("longvalue"));
    }


    @Test
    void floatValue() {
        assertEquals(12345.678f, resultSetMap.get("floatvalue"));
    }

    @Test
    void doubleValue() {
        assertEquals(123456789.123456789d, resultSetMap.get("doublevalue"));
    }

    @Test
    void bigDecimal() {
        assertEquals(new BigDecimal("12345.123"), resultSetMap.get("bigdecimalvalue"));
    }

    @Test
    void booleanValue() {
        assertEquals(true, resultSetMap.get("booleanvalue"));
    }

    @Test
    void booleanBitValue() {
        assertEquals(true, resultSetMap.get("bitvalue"));
    }

    @Test
    void dateValue() {
        assertEquals(LocalDate.of(2022, 8, 19), resultSetMap.get("datevalue"));
    }

    @Test
    void dateTimeValue() {
        assertEquals(LocalDateTime.of(2022, 8, 19, 7, 35, 12), resultSetMap.get("datetimevalue"));
    }

    @Test
    void timestampWithTimezone() throws SQLException {

        assertEquals(OffsetDateTime.of(2012, 8, 24, 14, 12, 1, 0, ZoneOffset.ofHours(2)), resultSetMap.get("timestampwithzonevalue"));
    }

    @Test
    void timeWithTimezone() throws SQLException {

        assertEquals(OffsetTime.of(15, 18, 4, 0, ZoneOffset.ofHours(3)), resultSetMap.get("timewithzonevalue"));
    }

    @Test
    void time() throws SQLException {

        assertEquals(LocalTime.of(16, 17, 19), resultSetMap.get("timevalue"));
    }

    @Test
    void varcharValue() {
        assertEquals("varcharvalue", resultSetMap.get("varcharvalue"));
    }

    @Test
    void charValue() {
        assertEquals("charvalue ", resultSetMap.get("charvalue"));
    }

    @Test
    void textValue() {
        assertEquals("textvalue", resultSetMap.get("textvalue"));
    }

    @Test
    void clobValue() {
        assertEquals("clobvalue", resultSetMap.get("clobvalue"));
    }


}