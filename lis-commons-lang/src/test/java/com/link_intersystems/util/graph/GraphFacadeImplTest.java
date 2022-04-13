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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphFacadeImplTest  {

    private Node start;
    private Node cRef;
    private Node dRef;
    private Node eRef;

    @BeforeEach
    public void createFacade() {
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
        start = new NodeImpl("A");
        start.addReference(new NodeImpl("B"));
        cRef = new NodeImpl("C");
        start.addReference(cRef);
        dRef = new NodeImpl("D");
        start.addReference(dRef);

        eRef = new NodeImpl("E");
        cRef.addReference(eRef);
        cRef.addReference(new NodeImpl("F"));

    }

    @Test
    void abstractClass() {
        new GraphFacade() {
        };
    }

    @Test
    void traverseBreathFirst() {
        TraverseAssertion traverseAssertion = new TraverseAssertion(Arrays.asList("A", "B", "C", "D", "E", "F"));
        GraphFacade.traverseBreadthFirst(start, traverseAssertion);
        traverseAssertion.assertAllUserObjectsTraversed();
    }

    @Test
    void traverseDepthFirst() {
        TraverseAssertion traverseAssertion = new TraverseAssertion(Arrays.asList("A", "B", "C", "E", "F", "D"));
        GraphFacade.traverseDepthFirst(start, traverseAssertion);
        traverseAssertion.assertAllUserObjectsTraversed();
    }

    @Test
    void cycleDetection() {
        eRef.addReference(cRef);
        assertThrows(CyclicGraphException.class, () -> GraphFacade.traverseDepthFirst(start, new CycleDetector()));
    }

    private static class TraverseAssertion implements Consumer<Node> {

        private final Iterator<String> userObjects;

        public TraverseAssertion(List<String> list) {
            this.userObjects = list.iterator();
        }

        public void accept(Node node) {
            Object next = userObjects.next();
            assertEquals(next, node.getUserObject());
        }

        public void assertAllUserObjectsTraversed() {
            boolean hasNext = userObjects.hasNext();
            String unexpectedNode = null;
            if (hasNext) {
                unexpectedNode = userObjects.next();
            }
            assertFalse("Unexpected node " + unexpectedNode + " traversed", hasNext);
        }
    }

}
