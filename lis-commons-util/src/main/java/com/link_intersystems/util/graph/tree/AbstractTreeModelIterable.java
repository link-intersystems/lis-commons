package com.link_intersystems.util.graph.tree;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class AbstractTreeModelIterable<T> implements Iterable<T> {
    protected TreeModel treeModel;
    protected T rootElement;

    public AbstractTreeModelIterable(TreeModel treeModel, T rootElement) {
        this.treeModel = requireNonNull(treeModel);
        this.rootElement = requireNonNull(rootElement);
    }

    @Override
    public Iterator<T> iterator() {
        return createIterator(treeModel, rootElement);
    }

    protected abstract Iterator<T> createIterator(TreeModel treeModel, T rootElement);
}
