package com.link_intersystems.util.graph.tree;

import java.util.*;
import java.util.stream.Stream;

/**
 * Iterates through a tree model by depth first strategy, but returns the elements in reverse order of the branches.
 * <p>
 * Assume the following tree structure. The number show the iteration order.
 *
 * <pre>
 *
 *          +-(2)--(1)
 *          |
 *      (7)-+-(3)
 *          |
 *          |     +-(4)
 *          |     |
 *          +-(6)-+
 *                |
 *                +-(5)
 * </pre>
 * <p>
 * You can {@link #setLeavesOnly(boolean)} to true and it will only iterate the leaves: 1, 4, 5.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DepthFirstBottomUpTreeModelIterable<T> extends AbstractTreeModelIterable<T> {

    private boolean leavesOnly;

    public DepthFirstBottomUpTreeModelIterable(TreeModel<T> treeModel, T rootElement) {
        super(treeModel, rootElement);
    }

    public void setLeavesOnly(boolean leavesOnly) {
        this.leavesOnly = leavesOnly;
    }

    @Override
    protected Iterator<T> createIterator(TreeModel<T> treeModel, T rootElement) {
        Stack<T> stack = new Stack<>();
        stack.push(rootElement);

        Set<T> childrenResolved = Collections.newSetFromMap(new IdentityHashMap<>());
        Map<Object, Boolean> isLeaf = new IdentityHashMap<>();

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

                while (true) {
                    T head = stack.peek();
                    if (childrenResolved.contains(head)) {
                        break;
                    }

                    Stream<? extends T> children = treeModel.getChildren(head);
                    int headIndex = stack.size();
                    children.forEach(c -> stack.add(headIndex, c));
                    childrenResolved.add(head);
                    boolean stackNotModified = headIndex == stack.size();
                    isLeaf.put(head, stackNotModified);

                    if (stackNotModified) {
                        break;
                    } else {
                        if (leavesOnly) stack.remove(headIndex - 1);
                    }
                }

                T next = stack.pop();

                return next;
            }
        };
    }


}
