package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AmbiguousJavaBeansFactory extends BeansFactory {
    @Override
    public String getTypeName() {
        return "java";
    }

    @Override
    public <T> BeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) throws BeanClassException {
        return null;
    }
}
