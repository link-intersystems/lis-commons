package com.link_intersystems.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class MapRowMapper implements RowMapper<Map<String, Object>> {

    private Supplier<Map<String, Object>> mapSupplier;

    public MapRowMapper() {
        this(LinkedHashMap::new);
    }

    public MapRowMapper(Supplier<Map<String, Object>> mapSupplier) {
        this.mapSupplier = Objects.requireNonNull(mapSupplier);
    }

    @Override
    public Map<String, Object> map(ResultSet resultSet) throws SQLException {
        Map<String, Object> row = mapSupplier.get();

        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
            String columnName = metaData.getColumnName(columnIndex);
            Object columnValue = resultSet.getObject(columnIndex);
            row.put(columnName, columnValue);
        }

        return row;
    }
}
