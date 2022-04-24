package com.link_intersystems.beans.java;

import com.link_intersystems.beans.IndexedPropertyDesc;

import java.beans.IndexedPropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaIndexedPropertyDesc extends JavaPropertyDesc implements IndexedPropertyDesc {

    private static final int SETTER_TYPE_PARAM_INDEX = 0;
    private static final int INDEXED_SETTER_TYPE_PARAM_INDEX = 1;

    private Class<?> type;

    public JavaIndexedPropertyDesc(IndexedPropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
    }

    public IndexedPropertyDescriptor getJavaPropertyDescriptor() {
        return (IndexedPropertyDescriptor) super.getJavaPropertyDescriptor();
    }

    /**
     * Returns true if this indexed property can be accessed through an indexed
     * getter method, e.g. <code>PropertyType getter(int index);</code>.
     *
     * @return true if this indexed property can be accessed through an indexed
     * getter method.
     */
    @Override
    public boolean isIndexedReadable() {
        return getJavaPropertyDescriptor().getIndexedReadMethod() != null;
    }

    /**
     * Returns true if this indexed property can be accessed through an indexed
     * setter method, e.g.
     * <code>void setter(int index, PropertyType value);</code>.
     *
     * @return true if this indexed property can be accessed through an indexed
     * setter method.
     */
    @Override
    public boolean isIndexedWritable() {
        return getJavaPropertyDescriptor().getIndexedWriteMethod() != null;
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
                    this.type = Array.newInstance(elementType, 0).getClass();
                }
            }
        }
        return type;
    }
}
