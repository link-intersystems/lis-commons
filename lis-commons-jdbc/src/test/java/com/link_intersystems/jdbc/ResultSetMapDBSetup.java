package com.link_intersystems.jdbc;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ResultSetMapDBSetup implements DBSetup {

    @Override
    public void setupSchema(Connection connection) throws SQLException {
    }

    @Override
    public void setupDdl(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("create table resultsetmap (" +
                    "id int primary key, " +
                    "shortvalue smallint, " +
                    "intvalue int, " +
                    "longvalue bigint, " +
                    "floatvalue real, " +
                    "doublevalue double precision, " +
                    "bigdecimalvalue numeric(10,3), " +
                    "booleanvalue  boolean, " +
                    "bitvalue  bit, " +
                    "timevalue time, " +
                    "timestampwithzonevalue timestamp with time zone, " +
                    "timewithzonevalue time with time zone, " +
                    "datevalue date, " +
                    "datetimevalue timestamp, " +
                    "varcharvalue varchar(255), " +
                    "charvalue char(10), " +
                    "textvalue text, " +
                    "clobvalue clob " +
                    ")");
        }
    }

    @Override
    public void setupData(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("insert into resultsetmap values(" +
                    "1, " +
                    Short.MAX_VALUE + ", " +
                    Integer.MAX_VALUE + ", " +
                    Long.MAX_VALUE + ", " +
                    "12345.678, " +
                    "123456789.123456789, " +
                    "12345.123, " +
                    "true, " +
                    "true, " +
                    "'16:17:19'," +
                    "'2012-08-24 14:12:01 +02:00', " +
                    "'15:18:04 +03:00', " +
                    "'2022-08-19', " +
                    "'2022-08-19 07:35:12', " +
                    "'varcharvalue', " +
                    "'charvalue', " +
                    "'textvalue', " +
                    "'clobvalue' " +
                    ")");
        }
    }
}
