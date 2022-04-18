package com.link_intersystems.util;

/**
 * @author - René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface Equality<T> {
    boolean isEqual(T o1, T o2);
}
