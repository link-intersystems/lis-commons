package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface IndexedProperty {

    PropertyDesc getDescriptor();

    boolean isIndexedReadable();

    boolean isIndexedWritable();

    <T> T getValue(int index);

    <T> void setValue(int index, T elementValue);
}
