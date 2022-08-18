package com.link_intersystems.jdbc;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class QualifiedTableName {
    private String catalog;
    private String schema;
    private String tableName;

    public QualifiedTableName(String tableName) {
        this(null, tableName);
    }

    public QualifiedTableName(String schema, String tableName) {
        this(null, schema, tableName);
    }

    public QualifiedTableName(String catalog, String schema, String tableName) {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = requireNonNull(tableName);
        if (tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("tableName must not be blank");
        }
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }
}
