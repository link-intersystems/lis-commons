/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.graph;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class DepthFirstNodeIteratorTest extends AbstractNodeIteratorTest {

    @Override
    protected Iterator<Node> createIterator(Node start) {
        return new DepthFirstNodeIterator(start);
    }

    @Override
    protected TraverseAssertion getTraverseAssertion(Node start) {
        TraverseAssertion traverseAssertion = new TraverseAssertion(Arrays.asList("A", "B", "C", "E", "F", "D"));
        return traverseAssertion;
    }

    @Test
    void nullArgumentConstructor() {
        assertThrows(NullPointerException.class, () -> new DepthFirstNodeIterator(null));
    }


    @Test
    void infiniteLoops() {
        start = new NodeImpl("A");
        NodeImpl bNode = new NodeImpl("B");
        start.addReference(bNode);
        bNode.addReference(start);

        Iterator<Node> iterator = new DepthFirstNodeIterator(start);

        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next().getUserObject());

        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next().getUserObject());

        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next().getUserObject());
    }

}
