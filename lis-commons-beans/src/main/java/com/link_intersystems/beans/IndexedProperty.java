package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface IndexedProperty<T> extends Property<T[]> {
    boolean isIndexedReadable();

    boolean isIndexedWritable();

    T getValue(int index);

    void setValue(int index, T elementValue);
}
