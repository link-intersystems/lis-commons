package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface IndexedProperty extends Property {

    public IndexedPropertyDesc getPropertyDesc();

    public <T> T getValue(int index);

    public <T> void setValue(int index, T elementValue);
}
