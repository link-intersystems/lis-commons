package com.link_intersystems.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ResultSetMapperTest {

    @Test
    void map() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        RowMapper<String> rowMapper = mock(RowMapper.class);

        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rowMapper.map(resultSet)).thenReturn("A").thenReturn("B");

        ResultSetMapper<String> resultSetMapper = new ResultSetMapper<>(rowMapper);

        List<String> result = resultSetMapper.map(resultSet);

        assertEquals(asList("A", "B"), result);
    }
}