package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanEventType {

    /**
     * The event name.
     */
    String getName();

    public BeanEvent newBeanEvent(Object bean);

    /**
     * Returns true if the given listener is applicable for this {@link BeanEvent},
     * which means that it can be added or removed.
     */
    boolean isApplicable(Object listener);

    boolean isApplicable(Class<?> listenerClass);

    /**
     * Adds the given listener to the provided bean if it supports this {@link BeanEventType}.
     * The provided bean supports this {@link BeanEventType} if it declares an add method for the given listener's type.
     * Otherwise it throws an {@link UnsupportedOperationException}.
     *
     * @param bean     the bean to add the listener to.
     * @param listener the listener to add.
     * @throws UnsupportedOperationException if the provided bean dosen't declare an add method
     *                                       for this {@link BeanEventType}.
     */
    void addListener(Object bean, Object listener) throws UnsupportedOperationException;

    /**
     * Removes the given listener from the provided bean if it supports this {@link BeanEventType}.
     * The provided bean supports this {@link BeanEventType} if it declares an remove method for the given listener's type.
     * Otherwise it throws an {@link UnsupportedOperationException}.
     *
     * @param bean     the bean to remove the listener from.
     * @param listener the listener to remove.
     * @throws UnsupportedOperationException if the provided bean dosen't declare a remove method
     *                                       for this {@link BeanEventType}.
     */
    void removeListener(Object bean, Object listener) throws UnsupportedOperationException;
}
