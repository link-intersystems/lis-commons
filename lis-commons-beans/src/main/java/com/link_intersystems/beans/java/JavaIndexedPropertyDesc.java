package com.link_intersystems.beans.java;

import com.link_intersystems.beans.IntedexPropertyDesc;
import com.link_intersystems.lang.reflect.ArrayType;

import java.beans.IndexedPropertyDescriptor;
import java.lang.reflect.Method;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaIndexedPropertyDesc<T> extends JavaPropertyDesc<T[]> implements IntedexPropertyDesc<T> {

    private static final int SETTER_TYPE_PARAM_INDEX = 0;
    private static final int INDEXED_SETTER_TYPE_PARAM_INDEX = 1;

    private Class<T[]> type;

    public JavaIndexedPropertyDesc(IndexedPropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
    }

    public IndexedPropertyDescriptor getJavaPropertyDescriptor() {
        return (IndexedPropertyDescriptor) super.getJavaPropertyDescriptor();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T[]> getType() {
        if (this.type == null) {
            IndexedPropertyDescriptor javaPropertyDescriptor = getJavaPropertyDescriptor();
            Method readMethod = javaPropertyDescriptor.getReadMethod();
            if (readMethod != null) {
                type = (Class<T[]>) readMethod.getReturnType();
            } else {
                Method writeMethod = javaPropertyDescriptor.getWriteMethod();
                if (writeMethod != null) {
                    type = (Class<T[]>) writeMethod.getParameterTypes()[SETTER_TYPE_PARAM_INDEX];
                } else {
                    Method indexedReadMethod = javaPropertyDescriptor.getIndexedReadMethod();
                    Class<T> elementType;
                    if (indexedReadMethod != null) {
                        elementType = (Class<T>) indexedReadMethod.getReturnType();
                    } else {
                        Method indexedWriteMethod = javaPropertyDescriptor.getIndexedWriteMethod();
                        Class<?>[] parameterTypes = indexedWriteMethod.getParameterTypes();
                        elementType = (Class<T>) parameterTypes[INDEXED_SETTER_TYPE_PARAM_INDEX];
                    }
                    ArrayType<T> arrayType = new ArrayType<>(elementType);
                    this.type = arrayType.getType();
                }
            }
        }
        return type;
    }
}
