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
package com.link_intersystems.util;

import java.util.ArrayList;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

public class UnmodifiableListIteratorTest {

	private UnmodifiableListIterator<String> unmodifiableListIterator;

	@Before
	public void setup() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("A");
		arrayList.add("B");
		arrayList.add("C");
		ListIterator<String> listIterator = arrayList.listIterator();
		unmodifiableListIterator = new UnmodifiableListIterator<String>(
				listIterator);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void add() {
		unmodifiableListIterator.add("A");
	}

	@Test
	public void hasNext() {
		unmodifiableListIterator.hasNext();
	}

	@Test
	public void hasPrevious() {
		unmodifiableListIterator.hasPrevious();
	}

	@Test
	public void next() {
		unmodifiableListIterator.next();
	}

	@Test
	public void nextIndex() {
		unmodifiableListIterator.nextIndex();
	}

	@Test
	public void previous() {
		unmodifiableListIterator.next();
		unmodifiableListIterator.previous();
	}

	@Test
	public void previousIndex() {
		unmodifiableListIterator.previousIndex();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void remove() {
		unmodifiableListIterator.remove();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void set() {
		unmodifiableListIterator.set("A");
	}

}
