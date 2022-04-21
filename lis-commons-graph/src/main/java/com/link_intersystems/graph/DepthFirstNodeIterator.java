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

import java.util.*;

/**
 * Iterator that implements the depth first traversal strategy for iterating
 * {@link Node}s.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
public class DepthFirstNodeIterator implements Iterator<Node> {

    private Stack<Object> stack = new Stack<Object>();

    private Node next;

    /**
     * New iterator that starts at the given {@link Node}.
     *
     * @param startNode the node to start the depth first traversal from.
     * @since 1.0.0;
     */
    public DepthFirstNodeIterator(Node startNode) {
        stack.push(Objects.requireNonNull(startNode, "startNode"));
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0;
     */
    @SuppressWarnings("unchecked")
    public boolean hasNext() {
        while (next == null && !stack.isEmpty()) {
            Object pop = stack.pop();
            if (pop instanceof Collection) {
                Collection<Node> collection = (Collection<Node>) pop;
                Iterator<Node> iterator = collection.iterator();
                int size = stack.size();
                stack.ensureCapacity(size + collection.size());
                while (iterator.hasNext()) {
                    Node reference = iterator.next();
                    stack.add(size, reference);
                }
                continue;
            }
            next = (Node) pop;
            Collection<Node> references = next.getReferences();
            if (!references.isEmpty()) {
                stack.push(references);
            }
        }
        return next != null;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0;
     */
    public Node next() {
        if (hasNext()) {
            Node tmp = next;
            next = null;
            return tmp;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Not implemented for {@link Node}s.
     *
     * @throws UnsupportedOperationException
     * @since 1.0.0;
     */
    public void remove() {
        throw new UnsupportedOperationException("remove operation is not supported.");
    }

}