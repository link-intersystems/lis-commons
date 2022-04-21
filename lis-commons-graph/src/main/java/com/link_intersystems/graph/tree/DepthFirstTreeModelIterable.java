package com.link_intersystems.graph.tree;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DepthFirstTreeModelIterable<T> extends AbstractTreeModelIterable<T> {

    public DepthFirstTreeModelIterable(TreeModel<T> treeModel, T rootElement) {
        super(treeModel, rootElement);
    }

    @Override
    protected Iterator<T> createIterator(TreeModel<T> treeModel, T rootElement) {
        Stack<T> stack = new Stack<>();
        stack.push(rootElement);

        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T current = stack.pop();

                Stream<? extends T> children = treeModel.getChildren(current);
                int headIndex = stack.size();
                children.forEach(c -> stack.add(headIndex, c));

                return current;
            }
        };
    }
}
