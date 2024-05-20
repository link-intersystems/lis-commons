package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface IndexedProperty extends Property {

    IndexedPropertyDesc getPropertyDesc();

    <T> T getValue(int index);

    <T> void setValue(int index, T elementValue);
}
