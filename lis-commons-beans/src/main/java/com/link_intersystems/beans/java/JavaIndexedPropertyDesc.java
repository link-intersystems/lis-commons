package com.link_intersystems.beans.java;

import com.link_intersystems.beans.IntedexPropertyDesc;
import com.link_intersystems.lang.reflect.ArrayType;

import java.beans.IndexedPropertyDescriptor;
import java.lang.reflect.Method;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaIndexedPropertyDesc extends JavaPropertyDesc implements IntedexPropertyDesc {

    private static final int SETTER_TYPE_PARAM_INDEX = 0;
    private static final int INDEXED_SETTER_TYPE_PARAM_INDEX = 1;

    private Class<?> type;

    public JavaIndexedPropertyDesc(IndexedPropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
    }

    public IndexedPropertyDescriptor getJavaPropertyDescriptor() {
        return (IndexedPropertyDescriptor) super.getJavaPropertyDescriptor();
    }

    @Override
    public Class<?> getType() {
        if (this.type == null) {
            IndexedPropertyDescriptor javaPropertyDescriptor = getJavaPropertyDescriptor();
            Method readMethod = javaPropertyDescriptor.getReadMethod();
            if (readMethod != null) {
                type = readMethod.getReturnType();
            } else {
                Method writeMethod = javaPropertyDescriptor.getWriteMethod();
                if (writeMethod != null) {
                    type = writeMethod.getParameterTypes()[SETTER_TYPE_PARAM_INDEX];
                } else {
                    Method indexedReadMethod = javaPropertyDescriptor.getIndexedReadMethod();
                    Class<?> elementType;
                    if (indexedReadMethod != null) {
                        elementType = indexedReadMethod.getReturnType();
                    } else {
                        Method indexedWriteMethod = javaPropertyDescriptor.getIndexedWriteMethod();
                        Class<?>[] parameterTypes = indexedWriteMethod.getParameterTypes();
                        elementType = parameterTypes[INDEXED_SETTER_TYPE_PARAM_INDEX];
                    }
                    ArrayType<?> arrayType = new ArrayType<>(elementType);
                    this.type = arrayType.getType();
                }
            }
        }
        return type;
    }
}
