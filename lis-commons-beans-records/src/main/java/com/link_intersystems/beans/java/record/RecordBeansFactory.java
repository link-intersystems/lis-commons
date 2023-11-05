package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeanClassException;
import com.link_intersystems.beans.BeansFactory;

import java.beans.IntrospectionException;

public class RecordBeansFactory extends BeansFactory {
    @Override
    public String getTypeName() {
        return "record";
    }

    @Override
    public <T> BeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) throws BeanClassException {
        /**
         * Ignoring stopClass, because records can not create an inheritance hierarchy.
         * See Java 17 JLS 8.10 https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.10
         */
        try {
            return new RecordBeanClass<>(beanClass);
        } catch (IntrospectionException e) {
            throw new BeanClassException("Unable to create record bean class", e);
        }
    }
}
