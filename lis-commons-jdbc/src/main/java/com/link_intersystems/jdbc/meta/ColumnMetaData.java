package com.link_intersystems.jdbc.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ColumnMetaData implements ColumnDescription {

    private String catalogName;
    private String schemaName;
    private String tableName;
    private String columnName;

    private int dataType;
    private String typeName;
    private int columnSize;
    private int decimalDigits;
    private int numPrecRadix;
    private int nullable;
    private String remarks;
    private String columnDefaultValue;
    private int charOctetLength;
    private final int ordinalPosition;
    private String isNullable;
    private String scopeCatalogName;
    private String scopeSchemaName;
    private String scopeTableName;
    private short sourceDataType;
    private String isAutoincrement;
    private String isGeneratedColumn;

    /**
     * @param columnResultSet a ResultSet as returned by {@link java.sql.DatabaseMetaData#getColumns(String, String, String, String)}.
     */
    public ColumnMetaData(ResultSet columnResultSet) throws SQLException {
        catalogName = columnResultSet.getString("TABLE_CAT");
        schemaName = columnResultSet.getString("TABLE_SCHEM");
        tableName = columnResultSet.getString("TABLE_NAME");
        columnName = columnResultSet.getString("COLUMN_NAME");
        dataType = columnResultSet.getInt("DATA_TYPE");
        typeName = columnResultSet.getString("TYPE_NAME");
        columnSize = columnResultSet.getInt("COLUMN_SIZE");
        decimalDigits = columnResultSet.getInt("DECIMAL_DIGITS");
        numPrecRadix = columnResultSet.getInt("NUM_PREC_RADIX");
        nullable = columnResultSet.getInt("NULLABLE");
        remarks = columnResultSet.getString("REMARKS");
        columnDefaultValue = columnResultSet.getString("COLUMN_DEF");
        charOctetLength = columnResultSet.getInt("CHAR_OCTET_LENGTH");
        ordinalPosition = columnResultSet.getInt("ORDINAL_POSITION");
        isNullable = columnResultSet.getString("IS_NULLABLE");
        scopeCatalogName = columnResultSet.getString("SCOPE_CATALOG");
        scopeSchemaName = columnResultSet.getString("SCOPE_SCHEMA");
        scopeTableName = columnResultSet.getString("SCOPE_TABLE");
        sourceDataType = columnResultSet.getShort("SOURCE_DATA_TYPE");
        isAutoincrement = columnResultSet.getString("IS_AUTOINCREMENT");
        isGeneratedColumn = columnResultSet.getString("IS_GENERATEDCOLUMN");
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public int getNullable() {
        return nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getColumnDefaultValue() {
        return columnDefaultValue;
    }

    public int getCharOctetLength() {
        return charOctetLength;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public String getScopeCatalogName() {
        return scopeCatalogName;
    }

    public String getScopeSchemaName() {
        return scopeSchemaName;
    }

    public String getScopeTableName() {
        return scopeTableName;
    }

    public short getSourceDataType() {
        return sourceDataType;
    }

    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public String getIsGeneratedColumn() {
        return isGeneratedColumn;
    }
}
