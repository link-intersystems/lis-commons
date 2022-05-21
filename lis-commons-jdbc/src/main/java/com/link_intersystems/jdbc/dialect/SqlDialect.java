package com.link_intersystems.jdbc.dialect;

import com.link_intersystems.jdbc.format.LiteralFormat;
import com.link_intersystems.jdbc.sql.InsertSql;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface SqlDialect {

    public InsertSql createInsertSql(String tableName);

    public LiteralFormat getLiteralFormat(Class<?> javaType);
}
