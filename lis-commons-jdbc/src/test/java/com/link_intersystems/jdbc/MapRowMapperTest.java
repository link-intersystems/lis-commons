package com.link_intersystems.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class MapRowMapperTest {

    @Test
    void map() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);

        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnName(1)).thenReturn("col1");
        when(metaData.getColumnName(2)).thenReturn("col2");
        when(resultSet.getObject(1)).thenReturn("A");
        when(resultSet.getObject(2)).thenReturn("B");

        MapRowMapper mapRowMapper = new MapRowMapper();

        Map<String, Object> row = mapRowMapper.map(resultSet);

        assertEquals(new HashMap<String, Object>() {
            {
                put("col1", "A");
                put("col2", "B");
            }
        }, row);
    }
}