package com.link_intersystems.sql.format.jdbc;

import com.link_intersystems.sql.format.LiteralFormat;
import com.link_intersystems.sql.format.LiteralFormatRegistry;
import com.link_intersystems.sql.format.SimpleLiteralFormat;
import com.link_intersystems.sql.statement.ColumnValue;
import com.link_intersystems.sql.statement.TableLiteralFormat;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ResultSetMetaDataTableLiteralFormat implements TableLiteralFormat {

    private ResultSetMetaData resultSetMetaData;

    private LiteralFormatRegistry<Integer> literalFormatRegistry = new JdbcLiteralFormatRegistry();
    private LiteralFormat defaultLiteralFormat = new SimpleLiteralFormat();

    public ResultSetMetaDataTableLiteralFormat(ResultSetMetaData resultSetMetaData) {
        this.resultSetMetaData = requireNonNull(resultSetMetaData);
    }

    public void setLiteralFormatRegistry(LiteralFormatRegistry<Integer> sqlTypeLiteralFormatRegistry) {
        this.literalFormatRegistry = requireNonNull(sqlTypeLiteralFormatRegistry);
    }

    public void setDefaultLiteralFormat(LiteralFormat defaultLiteralFormat) {
        this.defaultLiteralFormat = requireNonNull(defaultLiteralFormat);
    }

    @Override
    public String format(ColumnValue columnValue) throws Exception {
        String columnName = columnValue.getColumnName();

        int columnType = getColumnType(columnName);
        LiteralFormat literalFormat = literalFormatRegistry.getLiteralFormat(columnType);
        if (literalFormat == null) {
            literalFormat = defaultLiteralFormat;
        }

        return literalFormat.format(columnValue.getColumnValue());
    }

    private int getColumnType(String columnName) throws SQLException {
        int columnCount = resultSetMetaData.getColumnCount();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            String currColumnName = resultSetMetaData.getColumnName(columnIndex);
            if (currColumnName.equals(columnName)) {
                return resultSetMetaData.getColumnType(columnIndex);
            }
        }

        throw new IllegalArgumentException(columnName + " does not exists.");
    }
}
