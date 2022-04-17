package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface Property<T> {

    PropertyDesc<T> getDescriptor();

    T getValue() throws PropertyAccessException;

    void setValue(T propertyValue) throws PropertyAccessException;

}
