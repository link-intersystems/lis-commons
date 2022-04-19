package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeanClassException;
import com.link_intersystems.beans.BeansFactory;

import java.beans.IntrospectionException;
import java.text.MessageFormat;

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
        try {
            return new JavaBeanClass<>(beanClass, stopClass);
        } catch (IntrospectionException e) {
            String msg = MessageFormat.format("Unable to create BeanClass for ''{0}''. Stop at ''{1}''", beanClass, stopClass);
            throw new BeanClassException(msg, e);
        }
    }

}
