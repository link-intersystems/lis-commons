package com.link_intersystems.sql.statement;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class InsertSql {

    private String tableName;
    private List<ColumnValue> columnValues = new ArrayList<>();

    public InsertSql(String tableName) {
        this.tableName = Objects.requireNonNull(tableName);
    }

    public void addColumn(String columnName, Object value) {
        columnValues.add(new ColumnValue(columnName, value));
    }

    public String toSqlString(TableLiteralFormat tableLiteralFormat) throws Exception {
        StringBuilder buf = new StringBuilder(columnValues.size() * 15 + this.tableName.length() + 10);

        buf.append("insert into ").append(tableName);

        buf.append(" (");

        String columnNames = columnValues.stream()
                .map(ColumnValue::getColumnName)
                .collect(Collectors.joining(", "));
        buf.append(columnNames);

        buf.append(") values (");

        Iterator<ColumnValue> columnValueIterator = columnValues.iterator();
        while (columnValueIterator.hasNext()) {
            ColumnValue columnValue = columnValueIterator.next();
            String columnValueLiteral = tableLiteralFormat.format(columnValue);
            buf.append(columnValueLiteral);
            if (columnValueIterator.hasNext()) {
                buf.append(", ");
            }
        }

        buf.append(')');

        return buf.toString();
    }
}
