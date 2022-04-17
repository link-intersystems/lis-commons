package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanEvent {

    BeanEventType getType();

    void addListener(Object listener);

    void removeListener(Object listener);
}
