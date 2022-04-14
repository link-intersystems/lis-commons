package com.link_intersystems.util.graph.tree;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface TreeModel {

    Stream<?> getChildren(Object parent);

    default List<?> getChildrenList(Object parent) {
        return getChildren(parent).collect(Collectors.toList());
    }

    default boolean hasChildren(Object parent) {
        return getChildren(parent).findFirst().isPresent();
    }
}
