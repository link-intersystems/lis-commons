package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.BeanInstantiationException;
import com.link_intersystems.beans.java.JavaBeanClass;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public class RecordBeanClass<T> extends JavaBeanClass<T> {

    protected RecordBeanClass(Class<T> beanClass) throws IntrospectionException {
        this(Introspector.getBeanInfo(beanClass));
    }

    protected RecordBeanClass(BeanInfo recordBeanInfo) {
        super(recordBeanInfo);
    }


    @Override
    public RecordBean<T> getBeanFromInstance(T beanObject) {
        return new RecordBean<>(this, beanObject);
    }

    public RecordBean<T> newBeanInstance() {

        throw new BeanInstantiationException("Instantiation of Java records is not supported. Use getBeanFromInstance() instead.");
    }

}
