package com.link_intersystems.sql.dialect;

import com.link_intersystems.sql.format.LiteralFormat;
import com.link_intersystems.sql.statement.InsertSql;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface SqlDialect {

    public InsertSql createInsertSql(String tableName);

    public LiteralFormat getLiteralFormat(int sqlType);
}
