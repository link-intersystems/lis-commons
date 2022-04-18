package com.link_intersystems.beans.java;

import com.link_intersystems.beans.PropertyDesc;
import com.link_intersystems.beans.PropertyReadException;
import com.link_intersystems.beans.PropertyWriteException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaPropertyDesc extends JavaPropertyType implements PropertyDesc {

    public JavaPropertyDesc(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }
}
