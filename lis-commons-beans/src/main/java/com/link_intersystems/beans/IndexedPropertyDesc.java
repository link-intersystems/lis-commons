package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface IndexedPropertyDesc extends PropertyDesc {

    boolean isIndexedReadable();

    boolean isIndexedWritable();
}
