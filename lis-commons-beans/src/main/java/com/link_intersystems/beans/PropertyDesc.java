package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface PropertyDesc<T> {
    String getName();

    Class<T> getType();

    boolean isReadable();

    boolean isWritable();

    boolean isIndexed();

    Class<?> getDeclaringClass();
}
