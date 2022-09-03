package com.link_intersystems.sql.format;

import com.link_intersystems.sql.statement.ColumnValue;
import com.link_intersystems.sql.statement.TableLiteralFormat;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultTableLiteralFormat implements TableLiteralFormat {

    private LiteralFormat defaultLiteralFormat = new SimpleLiteralFormat();

    private Map<String, LiteralFormat> columnLiteralFormats = new HashMap<>();

    public void addLiteralFormat(String columnName, LiteralFormat literalFormat) {
        columnLiteralFormats.put(requireNonNull(columnName), requireNonNull(literalFormat));
    }

    public void setDefaultLiteralFormat(LiteralFormat defaultLiteralFormat) {
        this.defaultLiteralFormat = requireNonNull(defaultLiteralFormat);
    }

    @Override
    public String format(ColumnValue columnValue) throws Exception {
        String columnName = columnValue.getColumnName();
        LiteralFormat literalFormat = columnLiteralFormats.getOrDefault(columnName, defaultLiteralFormat);
        return literalFormat.format(columnValue.getColumnValue());
    }
}
