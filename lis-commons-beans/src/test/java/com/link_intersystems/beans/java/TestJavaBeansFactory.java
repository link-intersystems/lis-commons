package com.link_intersystems.beans.java;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TestJavaBeansFactory extends JavaBeansFactory implements TestBeansFactory {

    @Override
    public <T> JavaBeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) {
        return (JavaBeanClass<T>) super.createBeanClass(beanClass, stopClass);
    }

    @Override
    public <T> JavaBean<T> createBean(T bean) {
        return (JavaBean<T>) super.createBean(bean);
    }

    @Override
    public <T> JavaBean<T> createBean(T bean, Class<?> stopClass) {
        return (JavaBean<T>) super.createBean(bean, stopClass);
    }

    @Override
    public <T> JavaBeanClass<T> createBeanClass(Class<T> beanClass) {
        return (JavaBeanClass<T>) super.createBeanClass(beanClass);
    }

}
