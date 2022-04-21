package com.link_intersystems.graph.tree;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ItarableTreeModelTest {

    @Test
    public void getChildrenOfSingleObject() {
        IterableTreeModel itarableTreeModel = new IterableTreeModel();

        Stream<?> childrenStream = itarableTreeModel.getChildren("A");

        assertFalse(childrenStream.iterator().hasNext());
    }

    @Test
    public void getChildrenOfIterable() {
        IterableTreeModel itarableTreeModel = new IterableTreeModel();

        Stream<?> childrenStream = itarableTreeModel.getChildren(Collections.singletonList("A"));

        Iterator<?> iterator = childrenStream.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertFalse(iterator.hasNext());
    }

}