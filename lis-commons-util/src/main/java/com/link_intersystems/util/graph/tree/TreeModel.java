package com.link_intersystems.util.graph.tree;

import java.util.stream.Stream;

/**
 * A {@link TreeModel} describes a tree structure on objects.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface TreeModel<T> {

    /**
     * An object that can be used as a root object in case you don't have one.
     */
    public static final Object DEFAULT_ROOT = new Object();

    Stream<? extends T> getChildren(T parent);

    default <P extends T> boolean hasChildren(P parent) {
        return getChildren(parent).findFirst().isPresent();
    }
}
