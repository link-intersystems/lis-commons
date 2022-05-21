package com.link_intersystems.jdbc.meta;

public interface ColumnDescription {

    public String getCatalogName();

    public String getSchemaName();

    public String getTableName();

    public String getColumnName();
}