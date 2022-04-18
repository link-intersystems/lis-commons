package com.link_intersystems.beans.java;

import com.link_intersystems.beans.PropertyDesc;
import com.link_intersystems.beans.PropertyReadException;
import com.link_intersystems.beans.PropertyWriteException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaPropertyDesc implements PropertyDesc {

    private PropertyDescriptor propertyDescriptor;
    private Class<?> type;

    public JavaPropertyDesc(PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = requireNonNull(propertyDescriptor);
    }

    @Override
    public String getName() {
        return propertyDescriptor.getName();
    }

    @Override
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

    @Override
    public boolean isReadable() {
        return propertyDescriptor.getReadMethod() != null;
    }

    @Override
    public boolean isWritable() {
        return propertyDescriptor.getWriteMethod() != null;
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
        JavaPropertyDesc that = (JavaPropertyDesc) o;
        return Objects.equals(propertyDescriptor, that.propertyDescriptor) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyDescriptor, type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPropertyValue(Object bean) throws PropertyReadException {
        PropertyDescriptor javaPropertyDescriptor = getJavaPropertyDescriptor();
        Method readMethod = javaPropertyDescriptor.getReadMethod();
        if (readMethod == null) {
            throw new PropertyReadException(bean.getClass(), getName());
        }
        try {
            Object beanValue = invoke(readMethod, bean);
            return (T) beanValue;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new PropertyReadException(bean.getClass(), getName(), e);
        }
    }


    @Override
    public void setPropertyValue(Object bean, Object value) {
        PropertyDescriptor javaPropertyDescriptor = getJavaPropertyDescriptor();
        Method writeMethod = javaPropertyDescriptor.getWriteMethod();
        if (writeMethod == null) {
            throw new PropertyWriteException(bean.getClass(), getName());
        }
        try {
            invoke(writeMethod, bean, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new PropertyWriteException(bean.getClass(), getName(), e);
        }
    }

    protected Object invoke(Method method, Object target, Object... args) throws IllegalAccessException, InvocationTargetException {
        return method.invoke(target, args);
    }
}
