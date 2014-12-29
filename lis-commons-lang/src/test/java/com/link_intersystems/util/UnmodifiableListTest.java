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

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public abstract class UnmodifiableListTest extends UnmodifiableCollectionTest {

	protected final Collection<Object> getUnmodifiableCollection() {
		return getUnmodifiableList();
	}

	protected abstract List<Object> getUnmodifiableList();

	@Test
	public void getAt() {
		getUnmodifiableList().get(0);
	}

	@Test
	public void indexOf() {
		getUnmodifiableList().indexOf(getCollectionObject());
	}

	@Test
	public void lastIndexOf() {
		getUnmodifiableList().lastIndexOf(getCollectionObject());
	}

	@Test
	public void listIterator() {
		getUnmodifiableList().listIterator();
	}

	@Test
	public void listIteratorAt() {
		getUnmodifiableList().listIterator(0);
	}

	@Test
	public void subList() {
		getUnmodifiableList().subList(0, 1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addAt() {
		getUnmodifiableList().add(0, createComponentObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addAllAt() {
		getUnmodifiableList().addAll(0, createComponentObjects());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void listIteratorAdd() {
		ListIterator<Object> listIterator = getUnmodifiableList()
				.listIterator();
		listIterator.next();
		listIterator.add(createComponentObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void listIteratorSet() {
		ListIterator<Object> listIterator = getUnmodifiableList()
				.listIterator();
		listIterator.next();
		listIterator.set(createComponentObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void listIteratorRemove() {
		ListIterator<Object> listIterator = getUnmodifiableList()
				.listIterator();
		listIterator.next();
		listIterator.remove();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void listIteratorAtAdd() {
		ListIterator<Object> listIterator = getUnmodifiableList().listIterator(
				0);
		listIterator.next();
		listIterator.add(createComponentObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void listIteratorAtSet() {
		ListIterator<Object> listIterator = getUnmodifiableList().listIterator(
				0);
		listIterator.next();
		listIterator.set(createComponentObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void listIteratorAtRemove() {
		ListIterator<Object> listIterator = getUnmodifiableList().listIterator(
				0);
		listIterator.next();
		listIterator.remove();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void removeAt() {
		getUnmodifiableList().remove(0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setAt() {
		getUnmodifiableList().set(0, createComponentObject());
	}

}
