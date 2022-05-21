package com.link_intersystems.jdbc.sql;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface InsertSql {

    public void addColumn(String columnName, String literal) throws Exception;

    public String toSqlString();

}
