package com.link_intersystems.jdbc.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PrimaryKeyColumn implements Comparable<PrimaryKeyColumn> {
    private final String primaryKeyName;
    private final String catalogName;
    private final String schemaName;
    private final String tableName;
    private final String columnName;
    private final short keySequenceNumber;

    /**
     * @param primaryKeyResultSet a ResultSet as returned by {@link DatabaseMetaData#getPrimaryKeys(String, String, String)}.
     */
    public PrimaryKeyColumn(ResultSet primaryKeyResultSet) throws SQLException {
        catalogName = primaryKeyResultSet.getString("TABLE_CAT");
        schemaName = primaryKeyResultSet.getString("TABLE_SCHEM");
        tableName = primaryKeyResultSet.getString("TABLE_NAME");
        columnName = primaryKeyResultSet.getString("COLUMN_NAME");
        keySequenceNumber = primaryKeyResultSet.getShort("KEY_SEQ");
        primaryKeyName = primaryKeyResultSet.getString("PK_NAME");
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public short getKeySequenceNumber() {
        return keySequenceNumber;
    }

    @Override
    public int compareTo(PrimaryKeyColumn o) {
        int catalogCompare = nullSafeCompare(this::getCatalogName, o::getCatalogName);
        if (catalogCompare != 0) {
            return catalogCompare;
        }

        int schemaCompare = nullSafeCompare(this::getSchemaName, o::getSchemaName);
        if (schemaCompare != 0) {
            return catalogCompare;
        }

        int tableCompare = nullSafeCompare(this::getTableName, o::getTableName);
        if (tableCompare != 0) {
            return catalogCompare;
        }

        return Short.valueOf(getKeySequenceNumber()).compareTo(o.getKeySequenceNumber());
    }

    private <T extends Comparable<T>> int nullSafeCompare(Supplier<T> supplier1, Supplier<T> supplier2) {
        T o1 = supplier1.get();
        T o2 = supplier2.get();

        if (o1 == null) {
            return o2 == null ? 0 : -1;
        }

        if (o2 == null) {
            return 1;
        }

        return o1.compareTo(o2);
    }
}
