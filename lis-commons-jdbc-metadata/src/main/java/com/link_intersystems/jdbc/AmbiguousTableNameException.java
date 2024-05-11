package com.link_intersystems.jdbc;

import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AmbiguousTableNameException extends RuntimeException {

    private String tableName;

    public AmbiguousTableNameException(String tableName) {
        this.tableName = Objects.requireNonNull(tableName);
    }

    public String getTableName() {
        return tableName;
    }
}
