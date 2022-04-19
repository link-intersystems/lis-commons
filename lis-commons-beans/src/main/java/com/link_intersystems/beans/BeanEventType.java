package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanEventType {

    /**
     * The event name.
     */
    String getName();

    /**
     * Returns true if the given listener is applicable for this {@link BeanEvent},
     * which means that it can be added or removed.
     */
    boolean isApplicable(Object listener);

    boolean isApplicable(Class<?> listenerClass);
}
