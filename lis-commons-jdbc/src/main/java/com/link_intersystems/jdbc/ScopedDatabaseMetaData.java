package com.link_intersystems.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ScopedDatabaseMetaData {

    private final DatabaseMetaData databaseMetaData;
    private final String catalog;
    private final String schemaPattern;

    public ScopedDatabaseMetaData(DatabaseMetaData databaseMetaData, String catalog, String schemaPattern) {
        this.databaseMetaData = databaseMetaData;
        this.catalog = catalog;
        this.schemaPattern = schemaPattern;
    }

    public ResultSet getTables(String tableNamePattern) throws SQLException {
        return getTables(tableNamePattern, "TABLE");
    }

    public ResultSet getTables(String tableNamePattern, String... tableTypes) throws SQLException {
        return databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, tableTypes);
    }

    public ResultSet getColumns(String tableNamePattern) throws SQLException {
        return getColumns(tableNamePattern, "%");
    }

    public ResultSet getColumns(String tableNamePattern, String columnNamePattern) throws SQLException {
        return databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
    }

    public ResultSet getExportedKeys(String tableNamePattern) throws SQLException {
        return databaseMetaData.getExportedKeys(catalog, schemaPattern, tableNamePattern);
    }

    public ResultSet getImportedKeys(String tableNamePattern) throws SQLException {
        return databaseMetaData.getImportedKeys(catalog, schemaPattern, tableNamePattern);
    }

    public ResultSet getPrimaryKeys(String tableNamePattern) throws SQLException {
        return databaseMetaData.getPrimaryKeys(catalog, schemaPattern, tableNamePattern);
    }
}
