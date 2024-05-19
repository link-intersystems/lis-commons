package com.link_intersystems.beans;

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
     * Creates a new {@link AbstractBean} of this class that has
     * a new bean instance that can be retrieved by {@link AbstractBean#getBeanObject()}.
     */
    public AbstractBean<T> newBeanInstance() throws BeanInstantiationException {
        BeanInstanceFactory<T> beanInstanceFactory = getBeanInstanceFactory();
        return beanInstanceFactory.newBeanInstance();
    }

    protected abstract BeanInstanceFactory<T> getBeanInstanceFactory();

    /**
     * Returns a {@link AbstractBean} based on the given bean instance.
     */
    public AbstractBean<T> getBeanFromInstance(T beanObject) {
        BeanInstanceFactory<T> beanInstanceFactory = getBeanInstanceFactory();
        return beanInstanceFactory.fromExistingInstance(beanObject);
    }

    public abstract PropertyDescList getProperties();


    public BeanEventTypeList getBeanEventTypes() {
        return BeanEventTypeList.EMPTY;
    }
}
