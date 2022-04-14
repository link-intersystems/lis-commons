package com.link_intersystems.util.graph.tree;

import com.link_intersystems.util.Iterators;

import java.util.stream.Stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class IterableTreeModel implements TreeModel {

    @Override
    public Stream<?> getChildren(Object parent) {
        if (parent instanceof Iterable<?>) {
            Iterable<?> parentIterable = (Iterable<?>) parent;
            return Iterators.toStream(parentIterable);
        } else {
            return Stream.empty();
        }
    }
}
