package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.LiteralFormat;
import com.link_intersystems.sql.format.SimpleLiteralFormat;
import com.link_intersystems.sql.statement.ColumnValue;
import com.link_intersystems.sql.statement.TableLiteralFormat;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.BasicType;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.LiteralType;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateTableLiteralFormat implements TableLiteralFormat {

    private BasicTypeRegistry basicTypeRegistry = new BasicTypeRegistry();
    private Map<Class<?>, LiteralFormat> literalFormatOverrides = new HashMap<>();
    private LiteralFormat defaultLiteralFormat = SimpleLiteralFormat.INSTANCE;

    private Dialect dialect;

    public HibernateTableLiteralFormat(Dialect dialect) {
        this.dialect = dialect;
    }

    public void setDefaultLiteralFormat(LiteralFormat defaultLiteralFormat) {
        this.defaultLiteralFormat = requireNonNull(defaultLiteralFormat);
    }

    public void setLiteralFormatOverride(Class<?> type, LiteralFormat literalFormat) {
        literalFormatOverrides.put(requireNonNull(type), requireNonNull(literalFormat));
    }

    public Dialect getDialect() {
        return dialect;
    }

    @SuppressWarnings("rawtypes")
    public String format(ColumnValue columnValue) throws Exception {
        Object value = columnValue.getColumnValue();

        if (value == null) {
            return LiteralFormat.NULL_LITERAL;
        }

        Class<?> valueType = value.getClass();

        LiteralFormat literalFormat = literalFormatOverrides.get(valueType);
        if (literalFormat == null) {
            BasicType registeredType = basicTypeRegistry.getRegisteredType(valueType.getName());

            if (registeredType instanceof LiteralType) {
                LiteralType literalType = (LiteralType) registeredType;
                literalFormat = new HibernateLiteralFormat(literalType, getDialect());
            }
        }

        if (literalFormat == null) {

            literalFormat = defaultLiteralFormat;
        }

        return literalFormat.format(value);
    }
}
