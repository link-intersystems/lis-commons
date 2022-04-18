package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.PropertyDesc;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaPropertyDesc<T> implements PropertyDesc<T> {

    private PropertyDescriptor propertyDescriptor;
    private Class<T> type;

    public JavaPropertyDesc(PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = requireNonNull(propertyDescriptor);
    }

    @Override
    public String getName() {
        return propertyDescriptor.getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getType() {
        if (this.type == null) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                type = (Class<T>) readMethod.getReturnType();
            } else {
                Method writeMethod = propertyDescriptor.getWriteMethod();
                Class<?>[] parameterTypes = writeMethod.getParameterTypes();
                type = (Class<T>) parameterTypes[0];
            }
        }
        return type;
    }

    @Override
    public boolean isReadable() {
        return propertyDescriptor.getReadMethod() != null;
    }

    @Override
    public boolean isWritable() {
        return propertyDescriptor.getWriteMethod() != null;
    }

    @Override
    public boolean isIndexed() {
        return this.propertyDescriptor instanceof IndexedPropertyDescriptor;
    }

    public PropertyDescriptor getJavaPropertyDescriptor() {
        return propertyDescriptor;
    }

    @Override
    public Class<?> getDeclaringClass() {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();

        if (readMethod != null && writeMethod == null) {
            return readMethod.getDeclaringClass();
        } else if (readMethod == null && writeMethod != null) {
            return writeMethod.getDeclaringClass();
        } else if (readMethod != null) {
            Class<?> readDeclaringClass = readMethod.getDeclaringClass();
            Class<?> writeDeclaringClass = writeMethod.getDeclaringClass();

            if (readDeclaringClass.equals(writeDeclaringClass)
                    || readDeclaringClass.isAssignableFrom(writeDeclaringClass)) {
                return readDeclaringClass;
            } else {
                return writeDeclaringClass;
            }
        } else {
            String msg = MessageFormat.format("Can not determine declaring class of property {0}. " + //
                    "Read and write method is null.", this);
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaPropertyDesc<?> that = (JavaPropertyDesc<?>) o;
        return Objects.equals(propertyDescriptor, that.propertyDescriptor) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyDescriptor, type);
    }
}
