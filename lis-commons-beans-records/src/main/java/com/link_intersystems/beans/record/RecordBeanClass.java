package com.link_intersystems.beans.record;

import com.link_intersystems.beans.ArgumentResolver;
import com.link_intersystems.beans.BeanInstanceFactory;
import com.link_intersystems.beans.java.JavaBeanClass;
import com.link_intersystems.beans.java.record.Introspector;

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
    public BeanInstanceFactory<T> getBeanInstanceFactory(ArgumentResolver argumentResolver) {
        return new RecordBeanInstanceFactory<>(this, argumentResolver);
    }
}
