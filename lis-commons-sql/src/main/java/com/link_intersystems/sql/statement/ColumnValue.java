package com.link_intersystems.sql.statement;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ColumnValue {
    private String columnName;
    private Object columnValue;

    public ColumnValue(String columnName, Object columnValue) {
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getColumnValue() {
        return columnValue;
    }
}
