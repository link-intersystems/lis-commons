package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.AbstractLiteralFormat;
import com.link_intersystems.sql.format.LiteralFormat;
import com.link_intersystems.sql.format.ToStringLiteralFormat;
import com.link_intersystems.sql.statement.ColumnValue;
import com.link_intersystems.sql.statement.TableLiteralFormat;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.*;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.BasicJavaDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateTableLiteralFormat implements TableLiteralFormat {

    class SingleColumnTypeLiteralFormat extends AbstractLiteralFormat {
        private SingleColumnType singleColumnType;

        public SingleColumnTypeLiteralFormat(SingleColumnType singleColumnType) {
            this.singleColumnType = singleColumnType;
        }

        @Override
        protected String doFormat(Object value) {
            Function<Object, String> toStringSupplier = singleColumnType::toString;

            if (singleColumnType instanceof AbstractStandardBasicType) {
                AbstractStandardBasicType abstractStandardBasicType = (AbstractStandardBasicType) singleColumnType;
                SqlTypeDescriptor sqlTypeDescriptor = abstractStandardBasicType.getSqlTypeDescriptor();
                BasicJavaDescriptor<Object> jdbcRecommendedJavaTypeMapping = sqlTypeDescriptor.getJdbcRecommendedJavaTypeMapping(typeConfiguration);
                value = jdbcRecommendedJavaTypeMapping.wrap(value, getWrapperOptions());
                toStringSupplier = jdbcRecommendedJavaTypeMapping::toString;
            }

            return toStringSupplier.apply(value);
        }
    }

    private HibernateTypeMapping hibernateTypeMapping = new HibernateTypeMapping();
    private TypeConfiguration typeConfiguration = new TypeConfiguration();
    private BasicTypeRegistry basicTypeRegistry = typeConfiguration.getBasicTypeRegistry();
    private Map<String, LiteralFormat> literalFormatOverrides = new HashMap<>();
    private LiteralFormat defaultLiteralFormat = ToStringLiteralFormat.INSTANCE;
    private WrapperOptions wrapperOptions = new DefaultWrapperOptions();

    private Dialect dialect;

    public HibernateTableLiteralFormat(Dialect dialect) {
        this.dialect = dialect;
    }

    public void setDefaultLiteralFormat(LiteralFormat defaultLiteralFormat) {
        this.defaultLiteralFormat = requireNonNull(defaultLiteralFormat);
    }

    public void setLiteralFormatOverride(Class<?> type, LiteralFormat literalFormat) {
        literalFormatOverrides.put(requireNonNull(type).getName(), requireNonNull(literalFormat));
    }

    public void setWrapperOptions(WrapperOptions wrapperOptions) {
        this.wrapperOptions = requireNonNull(wrapperOptions);
    }

    BasicTypeRegistry getBasicTypeRegistry() {
        return basicTypeRegistry;
    }

    WrapperOptions getWrapperOptions() {
        return wrapperOptions;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public LiteralFormat getDefaultLiteralFormat() {
        return defaultLiteralFormat;
    }

    @SuppressWarnings("rawtypes")
    public String format(ColumnValue columnValue) throws Exception {
        Object value = columnValue.getColumnValue();

        if (value == null) {
            return LiteralFormat.NULL_LITERAL;
        }

        Class<?> valueType = value.getClass();
        String typeName = valueType.getName();

        LiteralFormat literalFormat = getLiteralFormat(typeName);

        return literalFormat.format(value);
    }

    protected LiteralFormat getLiteralFormat(String typeName) {
        return getLiteralFormat(typeName, this::getRemappedTypeLiteralFormat);
    }

    protected LiteralFormat getRemappedTypeLiteralFormat(BasicType basicType) {
        if (basicType instanceof SingleColumnType) {
            SingleColumnType singleColumnType = (SingleColumnType) basicType;
            int sqlType = singleColumnType.sqlType();
            String remappedType = hibernateTypeMapping.get(sqlType);
            return getLiteralFormat(remappedType, this::getDefaultLiteralFormat);
        }
        return null;
    }

    protected LiteralFormat getDefaultLiteralFormat(BasicType basicType) {
        if (basicType instanceof SingleColumnType) {
            SingleColumnType singleColumnType = (SingleColumnType) basicType;
            return new SingleColumnTypeLiteralFormat(singleColumnType);
        }
        return null;
    }

    private LiteralFormat getLiteralFormat(String typeName, Function<BasicType, LiteralFormat> unknownTypeHandler) {
        LiteralFormat literalFormat = null;

        LiteralFormat overrideLiteralFormat = literalFormatOverrides.get(typeName);
        if (overrideLiteralFormat != null) {
            literalFormat = overrideLiteralFormat;
        } else {
            BasicType registeredType = getBasicTypeRegistry().getRegisteredType(typeName);

            if (registeredType instanceof LiteralType) {
                LiteralType literalType = (LiteralType) registeredType;
                literalFormat = new HibernateLiteralFormat(literalType, getDialect(), wrapperOptions);
            }

            if (unknownTypeHandler != null) {
                literalFormat = unknownTypeHandler.apply(registeredType);
            }
        }

        if (literalFormat == null) {
            literalFormat = getDefaultLiteralFormat();
        }

        return literalFormat;
    }


}
