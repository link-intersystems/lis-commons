package com.link_intersystems.test.jdbc;

import org.junit.jupiter.api.Assertions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class RowAssertions {

    private Map<String, Object> row;

    private static Map<String, Object> toMap(ResultSet resultSet) throws SQLException {
        Map<String, Object> row = new LinkedHashMap<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            String columnName = metaData.getColumnName(columnIndex);
            row.put(columnName, resultSet.getObject(columnIndex));
        }

        return row;
    }

    public RowAssertions(ResultSet resultSet) throws SQLException {
        this(toMap(resultSet));
    }

    public RowAssertions(Map<String, Object> row) {
        this.row = row;
    }

    public void assertColumnValue(String columnName, Object expectedValue) {
        Object columnValue = row.get(columnName);
        assertEquals(expectedValue, columnValue, columnName + " value");
    }
}
