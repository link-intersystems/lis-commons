package com.link_intersystems.graph.tree;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * A {@link TreeModel} that expands parents that are {@link Iterator}s or {@link Iterable}s.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class IterableTreeModel<T> implements TreeModel<T> {

    @Override
    public Stream<? extends T> getChildren(T parent) {
        if (parent instanceof Iterable) {
            return Iterators.toStream(((Iterable<? extends T>) parent).iterator());
        }

        if (parent instanceof Iterator) {
            return Iterators.toStream((Iterator<? extends T>) parent);
        }

        return Stream.empty();
    }
}
