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

import java.util.List;
import java.util.Vector;

import org.junit.Test;

public abstract class UnmodifiableVectorTest extends UnmodifiableListTest {

	protected final List<Object> getUnmodifiableList() {
		return getUnmodifiableVector();
	}

	protected abstract Vector<Object> getUnmodifiableVector();

	@Test(expected = UnsupportedOperationException.class)
	public void addElement() {
		getUnmodifiableVector().addElement(createComponentObject());
	}

	@Test
	public void capacity() {
		getUnmodifiableVector().capacity();
	}

	@Test
	public void elementAt() {
		getUnmodifiableVector().elementAt(0);
	}

	@Test
	public void elements() {
		getUnmodifiableVector().elements();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void ensureCapacity() {
		getUnmodifiableVector().ensureCapacity(1024);
	}

	@Test
	public void firstElement() {
		getUnmodifiableVector().firstElement();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void insertElementAt() {
		getUnmodifiableVector().insertElementAt(createComponentObject(), 0);
	}

	@Test
	public void copyInto() {
		getUnmodifiableVector().copyInto(
				new Object[getUnmodifiableVector().size()]);
	}

	@Test
	public void lastElement() {
		getUnmodifiableVector().lastElement();
	}

	@Test
	public void lastIndexOfOffset() {
		getUnmodifiableVector().lastIndexOf(getCollectionObject(), 0);
	}

	@Test
	public void indexOfOffset() {
		getUnmodifiableVector().indexOf(getCollectionObject(), 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void removeAllElements() {
		getUnmodifiableVector().removeAllElements();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void removeElement() {
		getUnmodifiableVector().removeElement(getCollectionObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void removeElementAt() {
		getUnmodifiableVector().removeElementAt(0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setElementAt() {
		getUnmodifiableVector().setElementAt(createComponentObject(), 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void trimToSize() {
		getUnmodifiableVector().trimToSize();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setSize() {
		getUnmodifiableVector().setSize(getUnmodifiableVector().size() + 1);
	}

}
