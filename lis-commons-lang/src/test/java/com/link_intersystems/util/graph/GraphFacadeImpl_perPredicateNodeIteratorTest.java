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

import static java.util.Arrays.asList;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.link_intersystems.util.graph.AbstractNodeIteratorTest.TraverseAssertion;
import com.link_intersystems.util.graph.GraphFacade.NodeIterateStrategy;

class GraphFacadeImpl_perPredicateNodeIteratorTest  {

	NodeImpl A = new NodeImpl("A");
	NodeImpl B = new NodeImpl("B");
	NodeImpl C = new NodeImpl("C");
	NodeImpl D = new NodeImpl("D");
	NodeImpl E = new NodeImpl("E");
	NodeImpl F = new NodeImpl("F");
	NodeImpl G = new NodeImpl("G");
	NodeImpl H = new NodeImpl("H");
	NodeImpl I = new NodeImpl("I");
	NodeImpl J = new NodeImpl("J");
	NodeImpl K = new NodeImpl("K");
	NodeImpl L = new NodeImpl("L");
	NodeImpl M = new NodeImpl("M");
	NodeImpl N = new NodeImpl("N");
	private Predicate pred1;
	private Predicate pred2;
	private Predicate pred3;

	@BeforeEach
	public void createFacade() {
		/**
		 * <pre>
		 *                   A
		 *      +------------+------------+
		 *      B            C            D
		 * +----+----+       |        +---+------------+
		 * E    F    G       H        I   J            K
		 *                                      +------+------+
		 *                                      L      M      N
		 * </pre>
		 */

		A.addReference(B);
		A.addReference(C);
		A.addReference(D);

		B.addReference(E);
		B.addReference(F);
		B.addReference(G);

		C.addReference(H);

		D.addReference(I);
		D.addReference(J);
		D.addReference(K);

		K.addReference(L);
		K.addReference(M);
		K.addReference(N);

		pred1 = new Predicate() {

			public boolean test(Object object) {
				Node node = Node.class.cast(object);
				List<String> matchingNodes = asList("A", "C", "H", "D", "K");
				Object userObject = node.getUserObject();
				String userObjectAsString = userObject.toString();
				return matchingNodes.contains(userObjectAsString);
			}
		};
		pred2 = new Predicate() {

			public boolean test(Object object) {
				Node node = Node.class.cast(object);
				List<String> matchingNodes = asList("A", "B", "E", "F");
				Object userObject = node.getUserObject();
				String userObjectAsString = userObject.toString();
				return matchingNodes.contains(userObjectAsString);
			}
		};
		pred3 = new Predicate() {

			public boolean test(Object object) {
				Node node = Node.class.cast(object);
				List<String> matchingNodes = asList("D", "J", "M", "N");
				Object userObject = node.getUserObject();
				String userObjectAsString = userObject.toString();
				return matchingNodes.contains(userObjectAsString);
			}
		};
	}

	@Test
	void breadth_first() {
		List<String> expectedOrder = asList("A", "C", "D", "H", "K", "A", "B",
				"E", "F", "D", "J", "M", "N");
		TraverseAssertion traverseAssertion = new TraverseAssertion(
				expectedOrder);
		Iterator<Node> iterator = GraphFacade.perPredicateNodeIterator(
				NodeIterateStrategy.BREADTH_FIRST, A, pred1, pred2, pred3);
		GraphFacade.forAllDo(iterator, traverseAssertion);
		traverseAssertion.assertAllUserObjectsTraversed();
	}

	@Test
	void depth_first() {
		List<String> expectedOrder = asList("A", "C", "H", "D", "K", "A", "B",
				"E", "F", "D", "J", "M", "N");
		TraverseAssertion traverseAssertion = new TraverseAssertion(
				expectedOrder);
		Iterator<Node> iterator = GraphFacade.perPredicateNodeIterator(
				NodeIterateStrategy.DEPTH_FIRST, A, pred1, pred2, pred3);
		GraphFacade.forAllDo(iterator, traverseAssertion);
		traverseAssertion.assertAllUserObjectsTraversed();
	}

}
