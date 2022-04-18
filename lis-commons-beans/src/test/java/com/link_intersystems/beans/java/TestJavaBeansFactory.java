package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClassException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TestJavaBeansFactory extends JavaBeansFactory implements TestBeansFactory {

    @Override
    public <T> JavaBeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) throws BeanClassException {
        return (JavaBeanClass<T>) super.createBeanClass(beanClass, stopClass);
    }

    @Override
    public <T> JavaBean<T> createBean(T bean) throws BeanClassException {
        return (JavaBean<T>) super.createBean(bean);
    }

    @Override
    public <T> JavaBean<T> createBean(T bean, Class<?> stopClass) throws BeanClassException {
        return (JavaBean<T>) super.createBean(bean, stopClass);
    }

    @Override
    public <T> JavaBeanClass<T> createBeanClass(Class<T> beanClass) throws BeanClassException {
        return (JavaBeanClass<T>) super.createBeanClass(beanClass);
    }

}
