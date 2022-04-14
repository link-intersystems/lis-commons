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
/**
 *
 */
package com.link_intersystems.util.graph;

import java.util.function.Consumer;

/**
 * A {@link Consumer} that detects cycles when iterating a {@link Node} structure
 * and throws a {@link CyclicGraphException} if a cycle is detected.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public class NodeCycleDetector extends CycleDetector<Node> {

    @Override
    protected Object getIdentityObject(Node element) {
        return element.getUserObject();
    }

    @Override
    protected CycleException createCycleException(Node cycleCause) {
        return new CyclicGraphException(cycleCause);
    }
}