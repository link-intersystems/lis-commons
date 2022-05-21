package com.link_intersystems.jdbc.dialect;

import com.link_intersystems.jdbc.format.*;
import com.link_intersystems.jdbc.sql.InsertSql;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultSqlDialect implements SqlDialect {

    private Map<Class<?>, LiteralFormat> literalFormatRegistry = new HashMap<>();

    public DefaultSqlDialect() {
        register(String.class, new StringLiteralFormat());

        register(Timestamp.class, new TimestampLiteralFormat());
        register(Date.class, new TimestampLiteralFormat());
        register(java.sql.Date.class, new DateLiteralFormat());

        register(Integer.class, new ToStringLiteralFormat());
        register(BigDecimal.class, new BigDecimalLiteralFormat());
    }

    public void register(Class<?> javaType, LiteralFormat literalFormat) {
        literalFormatRegistry.put(javaType, literalFormat);
    }

    @Override
    public InsertSql createInsertSql(String tableName) {
        return new DefaultInsertSql(tableName);
    }

    @Override
    public LiteralFormat getLiteralFormat(Class<?> javaType) {
        LiteralFormat literalFormat = literalFormatRegistry.get(javaType);

        if (literalFormat == null) {
            throw new IllegalStateException("No LiteralFormat registered for " + javaType.getName());
        }

        return literalFormat;
    }
}
