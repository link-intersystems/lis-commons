package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanClass<T> {

    public abstract String getName();

    public abstract Class<T> getType();

    public default boolean isInstance(Object bean) {
        return getType().isInstance(bean);
    }

    /**
     * Creates a new {@link AbstractBean} of this class that has
     * a new bean instance that can be retrieved by {@link AbstractBean#getBeanObject()}.
     */
    public abstract Bean<T> newBeanInstance() throws BeanInstantiationException;

    /**
     * Returns a {@link AbstractBean} based on the given bean instance.
     */
    public abstract Bean<T> getBeanFromInstance(T beanObject);

    public abstract PropertyDescList getProperties();


    public default BeanEventTypeList getBeanEventTypes() {
        return BeanEventTypeList.EMPTY;
    }
}
