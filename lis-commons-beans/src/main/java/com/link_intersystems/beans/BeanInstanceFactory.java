package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanInstanceFactory<T> {

    default public AbstractBean<T> newBeanInstance(){
        return newBeanInstance(ArgumentResolver.NULL_INSTANCE);
    }

    public AbstractBean<T> newBeanInstance(ArgumentResolver argumentResolver);

    public AbstractBean<T> fromExistingInstance(T beanObject);
}
