package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.BeanInstanceFactory;
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
    public BeanInstanceFactory<T> getBeanInstanceFactory() {
        return new RecordBeanInstanceFactory<>(this);
    }
}
