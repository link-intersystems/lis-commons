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
package com.link_intersystems.util.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractNodeIteratorTest {

    protected Node start;
    protected Node cRef;
    protected Node dRef;
    protected Node eRef;
    protected Iterator<Node> nodeIterator;

    /**
     * <pre>
     *   +-> B
     *   |
     *   |     +-> E
     *   |     |
     * A +-> C +-> F
     *   |
     *   +-> D
     * </pre>
     */
    @BeforeEach
    public void before() {
        start = new NodeImpl("A");
        start.addReference(new NodeImpl("B"));
        cRef = new NodeImpl("C");
        start.addReference(cRef);
        dRef = new NodeImpl("D");
        start.addReference(dRef);

        eRef = new NodeImpl("E");
        cRef.addReference(eRef);
        cRef.addReference(new NodeImpl("F"));

        nodeIterator = createIterator(start);
    }

    protected abstract Iterator<Node> createIterator(Node start);

    protected abstract TraverseAssertion getTraverseAssertion(Node start);

    @Test
    void noSuchElementException() {
        while (nodeIterator.hasNext()) {
            nodeIterator.next();
        }
        assertThrows(NoSuchElementException.class, () -> nodeIterator.next());
    }

    @Test
    void removeUnsupported() {
        Assertions.assertTrue(nodeIterator.hasNext());
        Assertions.assertNotNull(nodeIterator.next());
        assertThrows(UnsupportedOperationException.class, () -> nodeIterator.remove());
    }

    @Test
    void traversal() {
        TraverseAssertion traverseAssertion = getTraverseAssertion(start);
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            traverseAssertion.accept(node);
        }
        traverseAssertion.assertAllUserObjectsTraversed();
    }

    protected static class TraverseAssertion implements Consumer<Node> {

        private final Iterator<String> userObjects;

        public TraverseAssertion(List<String> list) {
            this.userObjects = list.iterator();
        }

        public void accept(Node input) {
            Object next = userObjects.next();
            Assertions.assertEquals(next, input.getUserObject());
        }

        public void assertAllUserObjectsTraversed() {
            Assertions.assertFalse(userObjects.hasNext());
        }
    }
}