package com.link_intersystems.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ResultSetMap extends AbstractMap<String, Object> {

    private Map<String, Object> map = new HashMap<>();

    public ResultSetMap(ResultSet resultSet) throws SQLException {
        this(resultSet, new DefaultSqlTypeMapper());
    }

    public ResultSetMap(ResultSet resultSet, SqlTypeMapper sqlTypeMapper) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            String columnName = metaData.getColumnName(columnIndex);
            map.put(columnName, sqlTypeMapper.toObject(resultSet, columnIndex));
        }
    }


    public Set<Entry<String, Object>> entrySet() {
        return unmodifiableSet(map.entrySet());
    }
}
