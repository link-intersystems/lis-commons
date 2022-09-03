package com.link_intersystems.sql.statement;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface TableLiteralFormat {
    String format(ColumnValue columnValue) throws Exception;
}
