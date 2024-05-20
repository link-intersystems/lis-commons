package com.link_intersystems.beans;

import java.util.function.Predicate;

/**
 * A {@link Property} represents a property which is bound to a bean instance.
 * That's why you don't need to pass a bean instance when you want to set or get a value.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 * @see AbstractBean#getProperties()
 */
public interface Property {

    Predicate<? super Property> PREDICATE = Predicate.not(Property.INDEXED_PROPERTY_PREDICATE);
    Predicate<? super Property> INDEXED_PROPERTY_PREDICATE = jpd -> jpd instanceof IndexedProperty;

    PropertyDesc getPropertyDesc();

    <T> T getValue() throws PropertyReadException;

    void setValue(Object propertyValue) throws PropertyWriteException;

}
