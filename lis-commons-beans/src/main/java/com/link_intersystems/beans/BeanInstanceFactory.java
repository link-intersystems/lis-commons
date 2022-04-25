package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanInstanceFactory<T> {

    public Bean<T> newBeanInstance();

    public Bean<T> fromExistingInstance(T beanObject);
}
