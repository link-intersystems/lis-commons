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
/**
 *
 */
package com.link_intersystems.util.graph;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.collections4.Closure;

/**
 * A {@link Closure} that detects cycles when iterating a {@link Node} structure
 * and throws a {@link CyclicGraphException} if a cycle is detected.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public class CycleDetector implements Closure {

	private Collection<Node> nodesProcessing = new HashSet<Node>();

	/**
	 * {@inheritDoc}
	 *
	 * @param input
	 *            must be a {@link Node}.
	 * @since 1.0.0.0
	 */
	public void execute(Object input) {
		Node node = (Node) input;
		if (nodesProcessing.contains(node)) {
			throw new CyclicGraphException(node);
		}
		nodesProcessing.add(node);
	}

}