package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.LiteralFormat;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractStandardBasicType;
import org.hibernate.type.LiteralType;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateLiteralFormat implements LiteralFormat {

    private LiteralType literalType;
    private final Dialect dialect;
    private WrapperOptions wrapperOptions;

    public HibernateLiteralFormat(LiteralType literalType, Dialect dialect, WrapperOptions wrapperOptions) {
        this.literalType = literalType;
        this.dialect = dialect;
        this.wrapperOptions = wrapperOptions;
    }

    @Override
    public String format(Object value) throws Exception {
        if (literalType instanceof AbstractStandardBasicType) {
            AbstractStandardBasicType abstractStandardBasicType = (AbstractStandardBasicType) literalType;
            JavaTypeDescriptor javaTypeDescriptor = abstractStandardBasicType.getJavaTypeDescriptor();
            value = javaTypeDescriptor.wrap(value, wrapperOptions);
        }

        return literalType.objectToSQLString(value, dialect);
    }


}
