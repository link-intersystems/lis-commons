package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface Property<TYPE> {

    PropertyDesc<TYPE> getDescriptor();

    TYPE getValue();

    void setValue(TYPE propertyValue);

}
