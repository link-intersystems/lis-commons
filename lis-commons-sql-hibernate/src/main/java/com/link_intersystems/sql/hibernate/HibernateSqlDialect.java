package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.dialect.SqlDialect;
import com.link_intersystems.sql.format.LiteralFormat;
import com.link_intersystems.sql.statement.InsertSql;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.BasicType;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.LiteralType;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.Map;
import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateSqlDialect implements SqlDialect {

    private HibernateLiteralFormatOverride literalFormatOverride = new HibernateLiteralFormatOverride();
    private TypeConfiguration typeConfiguration = new TypeConfiguration();
    private Map<Integer, String> hibernateTypeMapping = new HibernateTypeMapping();
    private Dialect dialect;

    public HibernateSqlDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public void setHibernateTypeMapping(Map<Integer, String> hibernateTypeMapping) {
        this.hibernateTypeMapping = Objects.requireNonNull(hibernateTypeMapping);
    }

    @Override
    public InsertSql createInsertSql(String tableName) {
        return new HibernateInsertSql(dialect, tableName);
    }

    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public LiteralFormat getLiteralFormat(int sqlType) {
        String hibernateType = hibernateTypeMapping.get(sqlType);
        LiteralFormat literalFormat = literalFormatOverride.getLiteralType(dialect, hibernateType);

        if (literalFormat != null) {
            return literalFormat;
        }

        LiteralType literalType = getLiteralType(hibernateType);
        if (literalType == null) {
            throw new IllegalStateException("No LiteralFormat for sql type " + sqlType + " available.");
        }
        return new HibernateLiteralFormat(literalType, dialect);
    }

    private LiteralType<?> getLiteralType(String hibernateType) {


        BasicTypeRegistry basicTypeRegistry = typeConfiguration.getBasicTypeRegistry();
        BasicType basicType = basicTypeRegistry.getRegisteredType(hibernateType);
        LiteralType<?> literalType = null;
        if (basicType instanceof LiteralType) {
            literalType = (LiteralType<?>) basicType;
        }
        return literalType;
    }
}
