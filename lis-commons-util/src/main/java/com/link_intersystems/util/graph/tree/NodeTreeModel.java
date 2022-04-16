package com.link_intersystems.util.graph.tree;

import com.link_intersystems.util.Iterators;
import com.link_intersystems.util.graph.Node;

import java.util.stream.Stream;

/**
 * A {@link TreeModel} adaption for the {@link Node} interface.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class NodeTreeModel implements TreeModel<Node> {

    @Override
    public Stream<? extends Node> getChildren(Node parent) {
        return Iterators.toStream(parent.getReferences());
    }
}
