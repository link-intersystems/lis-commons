package com.link_intersystems.sql.statement;


import com.link_intersystems.sql.statement.InsertSql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultInsertSql implements InsertSql {

    private static class ColumnValue {
        private String columnName;
        private String columnValue;

        public ColumnValue(String columnName, String columnValue) {
            this.columnName = columnName;
            this.columnValue = columnValue;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getColumnValue() {
            return columnValue;
        }
    }

    private String tableName;
    private List<ColumnValue> columnValues = new ArrayList<>();

    public DefaultInsertSql(String tableName) {
        this.tableName = Objects.requireNonNull(tableName);
    }

    @Override
    public void addColumn(String columnName, String literal) {
        columnValues.add(new ColumnValue(columnName, literal));
    }

    @Override
    public String toSqlString() {
        StringBuilder buf = new StringBuilder(columnValues.size() * 15 + this.tableName.length() + 10);

        buf.append("insert into ").append(tableName);

        buf.append(" (");

        String columnNames = columnValues.stream()
                .map(ColumnValue::getColumnName)
                .collect(Collectors.joining(", "));
        buf.append(columnNames);

        buf.append(") values (");

        String columnValueLiterals = columnValues.stream()
                .map(ColumnValue::getColumnValue)
                .collect(Collectors.joining(", "));
        buf.append(columnValueLiterals);

        buf.append(')');

        return buf.toString();
    }
}
