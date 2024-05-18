package com.link_intersystems.beans;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class BeanClass<T> {

    private transient PropertyDescList properties;
    private transient PropertyDescList indexedProperties;

    public abstract String getName();

    public abstract Class<T> getType();

    public boolean isInstance(Object bean) {
        return getType().isInstance(bean);
    }

    /**
     * Creates a new {@link Bean} of this class that has
     * a new bean instance that can be retrieved by {@link Bean#getBeanObject()}.
     */
    public Bean<T> newBeanInstance() throws BeanInstantiationException {
        BeanInstanceFactory<T> beanInstanceFactory = getBeanInstanceFactory();
        return beanInstanceFactory.newBeanInstance();
    }

    protected abstract BeanInstanceFactory<T> getBeanInstanceFactory();

    /**
     * Returns a {@link Bean} based on the given bean instance.
     */
    public Bean<T> getBeanFromInstance(T beanObject) {
        BeanInstanceFactory<T> beanInstanceFactory = getBeanInstanceFactory();
        return beanInstanceFactory.fromExistingInstance(beanObject);
    }

    /**
     * @return the {@link #getSingleProperties()} and the {@link #getIndexedProperties()}.
     */
    public abstract PropertyDescList getProperties();


    public BeanEventTypeList getBeanEventTypes() {
        return BeanEventTypeList.EMPTY;
    }
}
