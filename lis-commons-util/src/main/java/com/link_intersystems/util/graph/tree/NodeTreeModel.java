package com.link_intersystems.util.graph.tree;

import com.link_intersystems.util.Iterators;
import com.link_intersystems.util.graph.Node;

import java.util.stream.Stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class NodeTreeModel implements TreeModel {
    @Override
    public Stream<?> getChildren(Object parent) {
        if (parent instanceof Node) {
            Node node = (Node) parent;
            return Iterators.toStream(node.getReferences());
        }
        return Stream.empty();
    }
}
