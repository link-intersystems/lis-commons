package com.link_intersystems.jdbc.dialect;

import com.link_intersystems.jdbc.format.*;
import com.link_intersystems.jdbc.sql.InsertSql;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultSqlDialect implements SqlDialect {

    private Map<Integer, LiteralFormat> literalFormatRegistry = new DefaultLiteralFormatRegistry();

    public void setLiteralFormatRegistry(Map<Integer, LiteralFormat> literalFormatRegistry) {
        this.literalFormatRegistry.clear();
        this.literalFormatRegistry.putAll(literalFormatRegistry);
    }

    @Override
    public InsertSql createInsertSql(String tableName) {
        return new DefaultInsertSql(tableName);
    }

    @Override
    public LiteralFormat getLiteralFormat(int sqlType) {
        LiteralFormat literalFormat = literalFormatRegistry.get(sqlType);

        if (literalFormat == null) {
            throw new IllegalStateException("No LiteralFormat registered for sql type " + sqlType);
        }

        return literalFormat;
    }
}
