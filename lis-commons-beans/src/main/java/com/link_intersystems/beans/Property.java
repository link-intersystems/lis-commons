package com.link_intersystems.beans;

/**
 * A {@link Property} represents a property which is bound to a bean instance.
 * That's why you don't need to pass a bean instance when you want to set or get a value.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 * @see Bean#getProperties()
 * @see Bean#getSingleProperties()
 * @see Bean#getIndexedProperties()
 */
public interface Property {

    PropertyDesc getPropertyDesc();

    <T> T getValue() throws PropertyReadException;

    void setValue(Object propertyValue) throws PropertyWriteException;

}
