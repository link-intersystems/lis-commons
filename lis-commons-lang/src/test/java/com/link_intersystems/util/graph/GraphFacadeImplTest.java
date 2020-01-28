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
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.Closure;
import org.junit.Before;
import org.junit.Test;

public class GraphFacadeImplTest {

	private Node start;
	private Node cRef;
	private Node dRef;
	private Node eRef;

	@Before
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
	public void abstractClass() {
		new GraphFacade() {
		};
	}

	@Test
	public void traverseBreathFirst() {
		TraverseAssertion traverseAssertion = new TraverseAssertion(
				Arrays.asList("A", "B", "C", "D", "E", "F"));
		GraphFacade.traverseBreadthFirst(start, traverseAssertion);
		traverseAssertion.assertAllUserObjectsTraversed();
	}

	@Test
	public void traverseDepthFirst() {
		TraverseAssertion traverseAssertion = new TraverseAssertion(
				Arrays.asList("A", "B", "C", "E", "F", "D"));
		GraphFacade.traverseDepthFirst(start, traverseAssertion);
		traverseAssertion.assertAllUserObjectsTraversed();
	}

	@Test(expected = CyclicGraphException.class)
	public void cycleDetection() {
		eRef.addReference(cRef);
		GraphFacade.traverseDepthFirst(start, new CycleDetector());
	}

	private static class TraverseAssertion implements Closure {

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
			boolean hasNext = userObjects.hasNext();
			String unexpectedNode = null;
			if (hasNext) {
				unexpectedNode = userObjects.next();
			}
			assertFalse("Unexpected node " + unexpectedNode + " traversed",
					hasNext);
		}
	}

}
