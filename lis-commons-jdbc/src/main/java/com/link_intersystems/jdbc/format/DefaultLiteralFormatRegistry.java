package com.link_intersystems.jdbc.format;

import java.sql.Types;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultLiteralFormatRegistry extends AbstractMap<Integer, LiteralFormat> {

    private Map<Integer, LiteralFormat> literalFormatBySqlType = new HashMap<>();

    public DefaultLiteralFormatRegistry() {
        VarcharLiteralFormat literalFormat = new VarcharLiteralFormat();
        put(literalFormat, Types.VARCHAR, Types.CHAR, Types.CLOB);

        put(new TimestampLiteralFormat(), Types.TIMESTAMP);
        put(new DateLiteralFormat(), Types.DATE);

        SimpleLiteralFormat toStringFormat = new SimpleLiteralFormat();
        put(toStringFormat, Types.INTEGER, Types.TINYINT, Types.SMALLINT, Types.BIGINT);

        DecimalLiteralFormat decimalFormat = new DecimalLiteralFormat();
        put(decimalFormat, Types.NUMERIC, Types.DECIMAL, Types.FLOAT, Types.DOUBLE);
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
