package com.link_intersystems.util.graph.tree;

import com.link_intersystems.util.Transformer;

import java.util.stream.Stream;

/**
 * A {@link TreeModel} that transforms each parent by a given {@link Transformer} before applying the
 * rules of a {@link IterableTreeModel}.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TransformedIterableTreeModel<T> extends IterableTreeModel<T> {

    private Transformer<T, T> transformer;

    public TransformedIterableTreeModel() {
        this(t -> t);
    }

    public TransformedIterableTreeModel(Transformer<T, T> transformer) {
        this.transformer = transformer;
    }

    @Override
    public Stream<? extends T> getChildren(T parent) {
        T transformed = transformer.transform(parent);
        return super.getChildren(transformed);
    }
}
