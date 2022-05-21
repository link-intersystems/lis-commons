package com.link_intersystems.jdbc.meta;

import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ColumnDescriptionEquality {

    private ColumnDescription columnDescription;

    public static boolean equals(ColumnDescription cd1, ColumnDescription cd2) {
        return Objects.equals(cd1.getCatalogName(), cd2.getCatalogName())
                && Objects.equals(cd1.getSchemaName(), cd2.getSchemaName())
                && Objects.equals(cd1.getTableName(), cd2.getTableName())
                && Objects.equals(cd1.getColumnName(), cd2.getColumnName());
    }

    public ColumnDescriptionEquality(ColumnDescription columnDescription) {
        this.columnDescription = columnDescription;
    }

    public boolean equalsDescription(ColumnDescription otherDescription) {
        return equals(columnDescription, otherDescription);
    }
}
