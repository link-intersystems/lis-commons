package com.link_intersystems.util.graph.tree;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DepthFirstTreeModelIterable<T> extends AbstractTreeModelIterable<T> {

    public DepthFirstTreeModelIterable(TreeModel treeModel, T rootElement) {
        super(treeModel, rootElement);
    }

    @Override
    protected Iterator<T> createIterator(TreeModel treeModel, T rootElement) {
        Stack<T> stack = new Stack<>();
        stack.push(rootElement);

        return new Iterator<T>() {
            private T current;

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                current = stack.pop();

                Stream<?> children = treeModel.getChildren(current);
                int headIndex = stack.size();
                children.forEach(c -> stack.add(headIndex, (T) c));

                return current;
            }
        };
    }
}
