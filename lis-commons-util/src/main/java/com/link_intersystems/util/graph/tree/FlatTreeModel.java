package com.link_intersystems.util.graph.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A {@link TreeModel} that flattens the tree structure for a given parent down to its leaves.
 * <p>
 * E.g. if the base model describes the following tree:
 * <pre>
 *
 *          +-(B)--(E)
 *          |
 *      (A)-+-(C)
 *          |
 *          |     +-(F)
 *          |     |
 *          +-(D)-+
 *                |
 *                +-(G)
 * </pre>
 * <p>
 * A {@link FlatTreeModel} of that base model will look like this:
 *
 * <pre>
 *          +-(E)
 *          |
 *      (A)-+-(C)
 *          |
 *          +-(F)
 *          |
 *          +-(G)
 * </pre>
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FlatTreeModel<T> implements TreeModel<T> {

    private TreeModel<T> baseModel;

    public FlatTreeModel(TreeModel<T> baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public Stream<? extends T> getChildren(T parent) {
        if (baseModel.hasChildren(parent)) {
            List<Object> allChildren = new ArrayList<>();

            Stream<? extends T> children = baseModel.getChildren(parent);
            children.forEach(c -> {
                if (baseModel.hasChildren(c)) {
                    getChildren(c).forEach(allChildren::add);
                } else {
                    allChildren.add(c);
                }
            });
            return (Stream<? extends T>) allChildren.stream();
        } else {
            return Stream.empty();
        }
    }
}
