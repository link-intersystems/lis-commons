package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface PropertyType {
    String getName();

    Class<?> getType();

    boolean isReadable();

    boolean isWritable();

    Class<?> getDeclaringClass();
}
