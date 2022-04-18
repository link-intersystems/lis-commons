package com.link_intersystems.beans.java;

import com.link_intersystems.beans.PropertyType;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaPropertyType implements PropertyType {
    private PropertyDescriptor propertyDescriptor;
    private Class<?> type;

    public JavaPropertyType(PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = requireNonNull(propertyDescriptor);
    }

    public String getName() {
        return propertyDescriptor.getName();
    }

    public Class<?> getType() {
        if (this.type == null) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                type = readMethod.getReturnType();
            } else {
                Method writeMethod = propertyDescriptor.getWriteMethod();
                Class<?>[] parameterTypes = writeMethod.getParameterTypes();
                type = parameterTypes[0];
            }
        }
        return type;
    }

    public boolean isReadable() {
        return propertyDescriptor.getReadMethod() != null;
    }

    public boolean isWritable() {
        return propertyDescriptor.getWriteMethod() != null;
    }

    public PropertyDescriptor getJavaPropertyDescriptor() {
        return propertyDescriptor;
    }

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
        JavaPropertyType that = (JavaPropertyType) o;
        return Objects.equals(propertyDescriptor, that.propertyDescriptor) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyDescriptor, type);
    }
}
