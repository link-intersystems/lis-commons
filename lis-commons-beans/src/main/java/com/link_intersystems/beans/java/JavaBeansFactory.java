package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeanClassException;
import com.link_intersystems.beans.BeansFactory;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaBeansFactory extends BeansFactory {

    public static final String TYPE_NAME = "java";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public <T> BeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) throws BeanClassException {
        return new JavaBeanClass<>(beanClass, stopClass);
    }

}
