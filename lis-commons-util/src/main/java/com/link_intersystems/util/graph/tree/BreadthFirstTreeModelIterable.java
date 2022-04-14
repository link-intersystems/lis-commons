package com.link_intersystems.util.graph.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BreadthFirstTreeModelIterable<T> extends AbstractTreeModelIterable<T> {

    public BreadthFirstTreeModelIterable(TreeModel treeModel, T rootElement) {
        super(treeModel, rootElement);
    }

    @Override
    protected Iterator<T> createIterator(TreeModel treeModel, T rootElement) {
        Queue<T> queue = new LinkedList<>();
        queue.offer(rootElement);

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }

            @Override
            public T next() {
                if (hasNext()) {
                    T next = queue.poll();
                    treeModel.getChildren(next).forEach(c -> queue.add((T) c));
                    return next;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
