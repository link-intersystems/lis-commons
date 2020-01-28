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
import java.util.ListIterator;

/**
 * An implementation of a {@link ListIterator} that throws
 * {@link UnsupportedOperationException} for operations that would modify the
 * underlying {@link List}.
 * 
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @param <E>
 */
public class UnmodifiableListIterator<E> implements ListIterator<E> {

	private final ListIterator<E> listIterator;

	public UnmodifiableListIterator(ListIterator<E> listIterator) {
		this.listIterator = listIterator;
	}

	/**
	 * @param o
	 * @see java.util.ListIterator#add(java.lang.Object)
	 * @throws UnsupportedOperationException
	 */
	public void add(E o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.ListIterator#hasNext()
	 */
	public boolean hasNext() {
		return listIterator.hasNext();
	}

	/**
	 * @return
	 * @see java.util.ListIterator#hasPrevious()
	 */
	public boolean hasPrevious() {
		return listIterator.hasPrevious();
	}

	/**
	 * @return
	 * @see java.util.ListIterator#next()
	 */
	public E next() {
		return listIterator.next();
	}

	/**
	 * @return
	 * @see java.util.ListIterator#nextIndex()
	 */
	public int nextIndex() {
		return listIterator.nextIndex();
	}

	/**
	 * @return
	 * @see java.util.ListIterator#previous()
	 */
	public E previous() {
		return listIterator.previous();
	}

	/**
	 * @return
	 * @see java.util.ListIterator#previousIndex()
	 */
	public int previousIndex() {
		return listIterator.previousIndex();
	}

	/**
	 * 
	 * @see java.util.ListIterator#remove()
	 * @throws UnsupportedOperationException
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param o
	 * @see java.util.ListIterator#set(java.lang.Object)
	 * @throws UnsupportedOperationException
	 */
	public void set(E o) {
		throw new UnsupportedOperationException();
	}

}
