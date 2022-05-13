package com.link_intersystems.jdbc;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultColumnDescription implements ColumnDescription {

    public static class Builder {
        private String catalogName;
        private String schemaName;
        private String tableName;
        private String columnName;

        public Builder setCatalogName(String catalogName) {
            this.catalogName = catalogName;
            return this;
        }

        public Builder setSchemaName(String schemaName) {
            this.schemaName = schemaName;
            return this;
        }

        public Builder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder setColumnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public DefaultColumnDescription build() {
            return new DefaultColumnDescription(catalogName, schemaName, tableName, columnName);
        }
    }

    private String catalogName;
    private String schemaName;
    private String tableName;
    private String columnName;

    private DefaultColumnDescription(String catalogName, String schemaName, String tableName, String columnName) {
        this.catalogName = catalogName;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    @Override
    public String getCatalogName() {
        return catalogName;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}
