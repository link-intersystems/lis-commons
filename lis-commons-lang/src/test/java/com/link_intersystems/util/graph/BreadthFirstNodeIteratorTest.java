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

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

public class BreadthFirstNodeIteratorTest extends AbstractNodeIteratorTest {

	@Override
	protected Iterator<Node> createIterator(Node start) {
		return new BreadthFirstNodeIterator(start);
	}

	@Override
	protected TraverseAssertion getTraverseAssertion(Node start) {
		TraverseAssertion traverseAssertion = new TraverseAssertion(
				Arrays.asList("A", "B", "C", "D", "E", "F"));
		return traverseAssertion;
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullArgumentConstructor() {
		new BreadthFirstNodeIterator(null);
	}
}
