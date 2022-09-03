package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.LiteralFormat;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.type.AbstractStandardBasicType;
import org.hibernate.type.LiteralType;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.util.TimeZone;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateLiteralFormat implements LiteralFormat {

    private LiteralType literalType;
    private final Dialect dialect;

    public HibernateLiteralFormat(LiteralType literalType, Dialect dialect) {
        this.literalType = literalType;
        this.dialect = dialect;
    }

    @Override
    public String format(Object value) throws Exception {
        if (literalType instanceof AbstractStandardBasicType) {
            AbstractStandardBasicType abstractStandardBasicType = (AbstractStandardBasicType) literalType;
            JavaTypeDescriptor javaTypeDescriptor = abstractStandardBasicType.getJavaTypeDescriptor();
            value = javaTypeDescriptor.wrap(value, NullWrapperOptions.INSTANCE);
        }

        return literalType.objectToSQLString(value, dialect);
    }

    private static class NullWrapperOptions implements WrapperOptions {

        public static final WrapperOptions INSTANCE = new NullWrapperOptions();

        @Override
        public boolean useStreamForLobBinding() {
            return false;
        }

        @Override
        public LobCreator getLobCreator() {
            return null;
        }

        @Override
        public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
            return null;
        }

        @Override
        public TimeZone getJdbcTimeZone() {
            return null;
        }
    }
}
