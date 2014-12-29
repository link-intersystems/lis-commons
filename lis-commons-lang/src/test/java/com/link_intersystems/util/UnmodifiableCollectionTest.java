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
import java.util.Collection;

import org.junit.Test;

public abstract class UnmodifiableCollectionTest {

	protected abstract Object createComponentObject();

	protected abstract Collection<Object> getUnmodifiableCollection();

	protected Collection<Object> createComponentObjects() {
		Collection<Object> collection = new ArrayList<Object>();
		for (int i = 0; i < 2; i++) {
			collection.add(createComponentObject());
		}
		return collection;
	}

	protected abstract Collection<Object> getCollectionObjects();

	protected abstract Object getCollectionObject();

	@Test
	public void contains() {
		getUnmodifiableCollection().contains(createComponentObject());
	}

	@Test
	public void containsAll() {
		getUnmodifiableCollection().containsAll(createComponentObjects());
	}

	@Test
	public void isEmpty() {
		getUnmodifiableCollection().isEmpty();
	}

	@Test
	public void iterator() {
		getUnmodifiableCollection().iterator();
	}

	@Test
	public void size() {
		getUnmodifiableCollection().size();
	}

	@Test
	public void toArray() {
		getUnmodifiableCollection().toArray();
	}

	@Test
	public void toTypedArray() {
		getUnmodifiableCollection().toArray(
				new Object[getUnmodifiableCollection().size()]);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void add() {
		getUnmodifiableCollection().add(createComponentObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addAll() {
		getUnmodifiableCollection().addAll(createComponentObjects());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void clear() {
		getUnmodifiableCollection().clear();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void remove() {
		getUnmodifiableCollection().remove(getCollectionObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void removeAll() {
		getUnmodifiableCollection().removeAll(getCollectionObjects());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void retainAll() {
		getUnmodifiableCollection().retainAll(getCollectionObjects());
	}

}
