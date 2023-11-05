package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanInstanceFactory<T> {

    default public Bean<T> newBeanInstance(){
        return newBeanInstance(ArgumentResolver.NULL_INSTANCE);
    }

    public Bean<T> newBeanInstance(ArgumentResolver argumentResolver);

    public Bean<T> fromExistingInstance(T beanObject);
}
