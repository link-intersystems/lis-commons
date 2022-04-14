/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.util.graph;

import java.util.Collection;

/**
 * Represents a node in a graph. Every node has an attached user object that
 * carries the information necessary for your concrete use case.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public interface Node {

	/**
	 * @return the nodes that this node references.
	 * @since 1.0.0.0
	 */
	public Collection<Node> getReferences();

	/**
	 * @return the user object that carries the information necessary for your
	 *         concrete use case.
	 * @since 1.0.0.0
	 */
	public Object getUserObject();

	/**
	 * Adds the given node as a reference of this node.
	 * 
	 * @param the
	 *            node to add as a reference for this node.
	 * @since 1.0.0.0
	 */
	public void addReference(Node node);

}
