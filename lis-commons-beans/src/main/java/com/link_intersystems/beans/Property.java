package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface Property {

    PropertyDesc getPropertyDesc();

    <T> T getValue() throws PropertyReadException;

    void setValue(Object propertyValue) throws PropertyWriteException;

}
