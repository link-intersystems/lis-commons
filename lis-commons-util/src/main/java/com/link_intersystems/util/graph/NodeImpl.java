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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of a {@link Node} that can be used to adapt any object
 * structure to a graph structure.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0;
 */
public class NodeImpl implements Node {

    private final Object userObject;

    private List<Node> references = new ArrayList<Node>();

    /*
     * references view only for clients
     */
    private List<Node> externalReferences = Collections
            .unmodifiableList(references);

    /**
     * Constructs a new node that holds the given user object.
     *
     * @param userObject
     */
    public NodeImpl(Object userObject) {
        this.userObject = userObject;
    }

    /**
     * {@inheritDoc}
     */
    public void addReference(Node node) {
        this.references.add(node);
    }

    /**
     * {@inheritDoc}
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Node> getReferences() {
        return externalReferences;
    }

    @Override
    public String toString() {
        return "NodeImpl{" +
                "userObject=" + userObject +
                '}';
    }
}
