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

    /**
     * A predicate that checks if a property is indexed.
     */
    public static final Predicate<? super Property> INDEXED = Property::isIndexedProperty;

    /**
     * A predicate that checks if a property is not indexed.
     */
    public static final Predicate<? super Property> NONE_INDEXED = Predicate.not(Property.INDEXED);

    public PropertyDesc getPropertyDesc();

    public <T> T getValue() throws PropertyReadException;

    public void setValue(Object propertyValue) throws PropertyWriteException;

    default public boolean isIndexedProperty() {
        return getIndexedProperty() != null;
    }

    default public IndexedProperty getIndexedProperty() {
        if (this instanceof IndexedProperty) {
            return (IndexedProperty) this;
        }
        return null;
    }

}
