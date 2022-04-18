package com.link_intersystems.beans.reflect;

import com.link_intersystems.beans.java.JavaBeanClass;
import com.link_intersystems.beans.java.JavaPropertyDescriptors;
import com.link_intersystems.lang.reflect.AbstractInvocationHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public abstract class AbstractBeanProxyHandler<T> extends AbstractInvocationHandler<T> {

    private JavaBeanClass<T> beanClass;

    protected AbstractBeanProxyHandler(Class<T> beanClass) throws IntrospectionException {
        super(beanClass);
        this.beanClass = new JavaBeanClass<>(beanClass);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PropertyDescriptor propertyDescriptor = beanClass.getPropertyDescriptor(method);
        if (propertyDescriptor == null) {
            method.invoke(this, args);
        } else {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (method.equals(readMethod)) {
                return handleGetMethod(method, propertyDescriptor);
            } else {
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (method.equals(writeMethod)) {
                    handleSetMethod(method, args, propertyDescriptor);
                }
            }
        }
        return null;
    }

    protected JavaPropertyDescriptors getPropertyDescriptors() {
        return beanClass.getJavaPropertyDescriptors();
    }

    protected JavaBeanClass<T> getBeanClass() {
        return beanClass;
    }

    protected abstract void handleSetMethod(Method method, Object[] args, PropertyDescriptor propertyDescriptor);

    protected abstract Object handleGetMethod(Method method, PropertyDescriptor propertyDescriptor);

}