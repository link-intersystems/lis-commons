package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.LiteralFormat;
import com.link_intersystems.sql.format.SimpleLiteralFormat;
import com.link_intersystems.sql.statement.ColumnValue;
import com.link_intersystems.sql.statement.TableLiteralFormat;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.BasicType;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.LiteralType;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateTableLiteralFormat implements TableLiteralFormat {

    private BasicTypeRegistry basicTypeRegistry = new BasicTypeRegistry();
    private Dialect dialect;

    public HibernateTableLiteralFormat(Dialect dialect) {
        this.dialect = dialect;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public String format(ColumnValue columnValue) throws Exception {
        Object value = columnValue.getColumnValue();

        if (value == null) {
            return LiteralFormat.NULL_LITERAL;
        }

        Class<?> valueType = value.getClass();

        BasicType registeredType = basicTypeRegistry.getRegisteredType(valueType.getName());

        if (registeredType instanceof LiteralType) {
            LiteralType literalType = (LiteralType) registeredType;
            HibernateLiteralFormat literalFormat = new HibernateLiteralFormat(literalType, getDialect());
            return literalFormat.format(value);
        }

        return SimpleLiteralFormat.INSTANCE.format(value);
    }
}
