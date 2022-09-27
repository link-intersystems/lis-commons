package com.link_intersystems.graph.tree;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A {@link TreeModel} that transforms each parent by a given {@link Function} before applying the
 * rules of a {@link IterableTreeModel}.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TransformedIterableTreeModel<T> extends IterableTreeModel<T> {

    private Function<T, T> transformer;

    public TransformedIterableTreeModel(Function<T, T> transformer) {
        this.transformer = transformer;
    }

    @Override
    public Stream<? extends T> getChildren(T parent) {
        T transformed = transformer.apply(parent);
        return super.getChildren(transformed);
    }
}
