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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.Closure;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractNodeIteratorTest {

	protected Node start;
	protected Node cRef;
	protected Node dRef;
	protected Node eRef;
	protected Iterator<Node> nodeIterator;

	@Before
	public void before() {
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

		nodeIterator = createIterator(start);
	}

	protected abstract Iterator<Node> createIterator(Node start);

	protected abstract TraverseAssertion getTraverseAssertion(Node start);

	@Test(expected = NoSuchElementException.class)
	public void noSuchElementException() {
		while (nodeIterator.hasNext()) {
			nodeIterator.next();
		}
		nodeIterator.next();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void removeUnsupported() {
		assertTrue(nodeIterator.hasNext());
		assertNotNull(nodeIterator.next());
		nodeIterator.remove();
	}

	@Test
	public void traversal() {
		TraverseAssertion traverseAssertion = getTraverseAssertion(start);
		while (nodeIterator.hasNext()) {
			Node node = (Node) nodeIterator.next();
			traverseAssertion.execute(node);
		}
		traverseAssertion.assertAllUserObjectsTraversed();
	}

	protected static class TraverseAssertion implements Closure {

		private final Iterator<String> userObjects;

		public TraverseAssertion(List<String> list) {
			this.userObjects = list.iterator();
		}

		public void execute(Object input) {
			assertTrue(input instanceof Node);
			Node node = (Node) input;
			Object next = userObjects.next();
			assertEquals(next, node.getUserObject());
		}

		public void assertAllUserObjectsTraversed() {
			assertFalse(userObjects.hasNext());
		}
	}
}