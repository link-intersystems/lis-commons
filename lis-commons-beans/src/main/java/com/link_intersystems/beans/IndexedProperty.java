package com.link_intersystems.beans;

import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface IndexedProperty extends Property {

    public static final Predicate<? super PropertyDesc> PREDICATE = jpd -> jpd instanceof IndexedPropertyDesc;

    IndexedPropertyDesc getPropertyDesc();

    <T> T getValue(int index);

    <T> void setValue(int index, T elementValue);
}
