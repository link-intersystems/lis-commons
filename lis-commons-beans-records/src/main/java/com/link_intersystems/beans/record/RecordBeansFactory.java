package com.link_intersystems.beans.record;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeanClassException;
import com.link_intersystems.beans.BeansFactory;

public class RecordBeansFactory extends BeansFactory {
    @Override
    public String getTypeName() {
        return "record";
    }

    @Override
    public <T> BeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) throws BeanClassException {
        return null;
    }
}
