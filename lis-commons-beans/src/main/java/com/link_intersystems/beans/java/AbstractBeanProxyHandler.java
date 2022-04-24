package com.link_intersystems.beans.java;

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
        JavaPropertyDesc propertyDesc = beanClass.getPropertyDescByMethod(method);

        if (propertyDesc == null) {
            method.invoke(this, args);
        } else {
            PropertyDescriptor javaPropertyDescriptor = propertyDesc.getJavaPropertyDescriptor();

            if (propertyDesc.isReadMethod(method)) {
                return handleGetMethod(method, javaPropertyDescriptor);
            } else {
                handleSetMethod(method, args, javaPropertyDescriptor);
            }
        }
        return null;
    }

    protected JavaBeanClass<T> getBeanClass() {
        return beanClass;
    }

    protected abstract void handleSetMethod(Method method, Object[] args, PropertyDescriptor propertyDescriptor);

    protected abstract Object handleGetMethod(Method method, PropertyDescriptor propertyDescriptor);

}