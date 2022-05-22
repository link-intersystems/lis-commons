package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.statement.InsertSql;
import org.hibernate.dialect.Dialect;
import org.hibernate.sql.Insert;

import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateInsertSql implements InsertSql {

    private Insert insert;

    public HibernateInsertSql(Dialect dialect, String tableName) {
        insert = new Insert(dialect);
        insert.setTableName(Objects.requireNonNull(tableName));
    }

    @Override
    public void addColumn(String columnName, String literal) throws Exception {
        insert.addColumn(columnName, literal);
    }

    @Override
    public String toSqlString() {
        return insert.toStatementString();
    }
}
