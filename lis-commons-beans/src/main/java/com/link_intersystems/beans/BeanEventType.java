package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanEventType {
    String getName();

    boolean isApplicable(Object listener);

    boolean isApplicable(Class<?> listenerClass);
}
