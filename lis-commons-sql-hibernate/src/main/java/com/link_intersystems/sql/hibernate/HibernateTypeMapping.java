package com.link_intersystems.sql.hibernate;

import java.sql.Types;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateTypeMapping extends AbstractMap<Integer, String> {

    private Map<Integer, String> hibernateTypeByJavaType = new HashMap<>();

    public HibernateTypeMapping() {
        hibernateTypeByJavaType.put(Types.VARCHAR, "string");
        hibernateTypeByJavaType.put(Types.CHAR, "string");

        hibernateTypeByJavaType.put(Types.INTEGER, "integer");
        hibernateTypeByJavaType.put(Types.BIGINT, "long");
        hibernateTypeByJavaType.put(Types.TINYINT, "byte");
        hibernateTypeByJavaType.put(Types.SMALLINT, "short");
        hibernateTypeByJavaType.put(Types.FLOAT, "float");
        hibernateTypeByJavaType.put(Types.DOUBLE, "double");

        hibernateTypeByJavaType.put(Types.DATE, "date");
        hibernateTypeByJavaType.put(Types.TIME, "time");
        hibernateTypeByJavaType.put(Types.TIMESTAMP, "timestamp");
        hibernateTypeByJavaType.put(Types.TIMESTAMP_WITH_TIMEZONE, "calendar");

        hibernateTypeByJavaType.put(Types.DECIMAL, "big_decimal");
        hibernateTypeByJavaType.put(Types.NUMERIC, "big_decimal");

        hibernateTypeByJavaType.put(Types.BLOB, "blob");
        hibernateTypeByJavaType.put(Types.VARBINARY, "blob");
        hibernateTypeByJavaType.put(Types.LONGVARBINARY, "blob");
        hibernateTypeByJavaType.put(Types.CLOB, "clob");
        hibernateTypeByJavaType.put(Types.LONGNVARCHAR, "clob");

        hibernateTypeByJavaType.put(Types.BOOLEAN, "boolean");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return hibernateTypeByJavaType.entrySet();
    }
}
