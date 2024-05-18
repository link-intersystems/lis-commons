package com.link_intersystems.beans;

import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface IndexedPropertyDesc extends PropertyDesc {

    public static final Predicate<? super PropertyDesc> PREDICATE = pd -> pd instanceof IndexedPropertyDesc;

    boolean isIndexedReadable();

    boolean isIndexedWritable();
}
