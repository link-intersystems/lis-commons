package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.Bean;
import com.link_intersystems.beans.BeanInstanceFactory;
import com.link_intersystems.beans.BeanInstantiationException;
import com.link_intersystems.beans.java.JavaBeanClass;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public class RecordBeanClass<T> extends JavaBeanClass<T> {

    private BeanInstanceFactory<T> beanInstanceFactory = new RecordBeanInstanceFactory<>(this);

    protected RecordBeanClass(Class<T> beanClass) throws IntrospectionException {
        this(Introspector.getBeanInfo(beanClass));
    }

    protected RecordBeanClass(BeanInfo recordBeanInfo) {
        super(recordBeanInfo);
    }


    @Override
    public Bean<T> newBeanInstance() throws BeanInstantiationException {
        return beanInstanceFactory.newBeanInstance();
    }

    @Override
    public Bean<T> getBeanFromInstance(T beanObject) {
        return beanInstanceFactory.fromExistingInstance(beanObject);
    }
}
