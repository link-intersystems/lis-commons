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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import com.link_intersystems.lang.Assert;

/**
 * Iterator that implements the breadth first traversal strategy for iterating
 * {@link Node}s.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public class BreadthFirstNodeIterator implements Iterator<Node> {

	private Queue<Node> queue = new LinkedList<Node>();

	/**
	 * New iterator that starts at the given {@link Node}.
	 *
	 * @param startNode
	 *            the node to start the breadth first traversal from.
	 * @since 1.0.0.0
	 */
	public BreadthFirstNodeIterator(Node startNode) {
		Assert.notNull("startNode", startNode);
		queue.offer(startNode);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	public boolean hasNext() {
		return !queue.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	public Node next() {
		if (hasNext()) {
			Node next = queue.poll();
			Collection<Node> references = next.getReferences();
			queue.addAll(references);
			return next;
		} else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Not implemented for {@link Node}s.
	 *
	 * @throws UnsupportedOperationException
	 * @since 1.0.0.0
	 */
	public void remove() {
		throw new UnsupportedOperationException(
				"remove operation is not supported.");
	}

}