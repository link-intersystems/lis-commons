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
    public abstract Bean<T> newBeanInstance() throws BeanInstantiationException ;

    /**
     * Returns a {@link AbstractBean} based on the given bean instance.
     */
    public abstract Bean<T> getBeanFromInstance(T beanObject);

    public abstract PropertyDescList getProperties();


    public BeanEventTypeList getBeanEventTypes() {
        return BeanEventTypeList.EMPTY;
    }
}
