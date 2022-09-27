package com.link_intersystems.sql.format.jdbc;

import com.link_intersystems.sql.format.*;

import java.sql.Types;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JdbcLiteralFormatRegistry extends AbstractMap<Integer, LiteralFormat> implements LiteralFormatRegistry<Integer> {

    private Map<Integer, LiteralFormat> literalFormatBySqlType = new HashMap<>();
    private LiteralFormat defaultLiteralFormat = ToStringLiteralFormat.INSTANCE;

    public JdbcLiteralFormatRegistry() {
        QuotedStringLiteralFormat literalFormat = new QuotedStringLiteralFormat();
        put(literalFormat, Types.VARCHAR, Types.CHAR, Types.CLOB);

        put(new TimestampLiteralFormat(), Types.TIMESTAMP);
        put(new DateLiteralFormat(), Types.DATE);

        DecimalLiteralFormat decimalFormat = new DecimalLiteralFormat();
        put(decimalFormat, Types.NUMERIC, Types.DECIMAL, Types.FLOAT, Types.DOUBLE);
    }

    @Override
    public LiteralFormat getLiteralFormat(Integer typeDescriptor) {
        return getOrDefault(typeDescriptor, defaultLiteralFormat);
    }

    @Override
    public LiteralFormat put(Integer key, LiteralFormat value) {

        return literalFormatBySqlType.put(key, value);
    }

    public void put(LiteralFormat literalFormat, int... sqlTypes) {
        for (int sqlType : sqlTypes) {
            put(sqlType, literalFormat);
        }
    }

    @Override
    public Set<Entry<Integer, LiteralFormat>> entrySet() {
        return literalFormatBySqlType.entrySet();
    }
}
