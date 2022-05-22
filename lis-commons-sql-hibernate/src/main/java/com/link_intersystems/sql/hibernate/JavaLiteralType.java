package com.link_intersystems.sql.hibernate;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.LiteralType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;

public class JavaLiteralType<T> implements LiteralType<T> {

    private JavaTypeDescriptor<T> descriptor;

    public JavaLiteralType(JavaTypeDescriptor<T> javaTypeDescriptor) {
        descriptor = javaTypeDescriptor;
    }

    public Class<T> getJavaType() {
        return descriptor.getJavaType();
    }

    @Override
    public String objectToSQLString(T value, Dialect dialect) throws Exception {
        return value == null ? "null" : descriptor.toString(value);
    }

}