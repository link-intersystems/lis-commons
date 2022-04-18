package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClassException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface TestBeansFactory {
    <T> JavaBeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) throws BeanClassException;

    <T> JavaBean<T> createBean(T bean) throws BeanClassException;

    <T> JavaBean<T> createBean(T bean, Class<?> stopClass) throws BeanClassException;

    <T> JavaBeanClass<T> createBeanClass(Class<T> beanClass) throws BeanClassException;
}
