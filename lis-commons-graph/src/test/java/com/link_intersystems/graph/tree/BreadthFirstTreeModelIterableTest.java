package com.link_intersystems.graph.tree;

import com.link_intersystems.graph.AbstractNodeIteratorTest;
import com.link_intersystems.graph.Node;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BreadthFirstTreeModelIterableTest extends AbstractNodeIteratorTest {
    @Override
    protected Iterator<Node> createIterator(Node start) {
        return new BreadthFirstTreeModelIterable<>(new NodeTreeModel(), start).iterator();
    }

    @Override
    protected TraverseAssertion getTraverseAssertion(Node start) {
        TraverseAssertion traverseAssertion = new TraverseAssertion(Arrays.asList("A", "B", "C", "D", "E", "F"));
        return traverseAssertion;
    }
}
