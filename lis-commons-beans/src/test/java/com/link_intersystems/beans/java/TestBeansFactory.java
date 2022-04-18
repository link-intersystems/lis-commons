package com.link_intersystems.beans.java;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface TestBeansFactory {
    <T> JavaBeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass);

    <T> JavaBean<T> createBean(T bean);

    <T> JavaBean<T> createBean(T bean, Class<?> stopClass);

    <T> JavaBeanClass<T> createBeanClass(Class<T> beanClass);
}
