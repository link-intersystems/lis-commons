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
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

/**
 * A {@link Stack} implementation that can not be modified. Every attempt to
 * modify it will result in an {@link UnsupportedOperationException}.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 *            the stack's element type
 * @since 1.0.0.0
 */
class UnmodifiableStack<T> extends Stack<T> implements Cloneable {

	private static final String UNMODIFIABLE = "unmodifiable";

	private static final long serialVersionUID = -503112464230409463L;

	private Stack<T> stack;

	private List<T> unmodifiableStackList;

	UnmodifiableStack(Stack<T> stack) {
		init(stack);
	}

	private void init(Stack<T> stack) {
		this.stack = stack;
		unmodifiableStackList = Collections.unmodifiableList(stack);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void addElement(T obj) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public int capacity() {
		return stack.capacity();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@SuppressWarnings("unchecked")
	public Object clone() {
		UnmodifiableStack<T> unmodifiableClone = (UnmodifiableStack<T>) super.clone();
		unmodifiableClone.init((Stack<T>) stack.clone());
		return unmodifiableClone;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public void copyInto(Object[] anArray) {
		stack.copyInto(anArray);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public T elementAt(int index) {
		return stack.elementAt(index);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public Enumeration<T> elements() {
		return stack.elements();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public T lastElement() {
		return stack.lastElement();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public int lastIndexOf(Object elem, int index) {
		return stack.lastIndexOf(elem, index);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public boolean empty() {
		return stack.empty();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void ensureCapacity(int minCapacity) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public T firstElement() {
		return stack.firstElement();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public T get(int index) {
		return stack.get(index);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public int indexOf(Object elem, int index) {
		return stack.indexOf(elem, index);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public T peek() {
		return stack.peek();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public T pop() {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public T push(T item) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void removeAllElements() {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public boolean removeElement(Object obj) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void removeElementAt(int index) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public int search(Object o) {
		return stack.search(o);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void setElementAt(T obj, int index) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void setSize(int newSize) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public String toString() {
		return stack.toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void trimToSize() {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/*
	 * Unmodifiable list view on stack
	 */

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void add(int index, T element) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}.
	 *
	 * Every attempt to modify the returned ListIterator will cause an
	 * {@link UnsupportedOperationException}
	 */
	public ListIterator<T> listIterator() {
		return unmodifiableStackList.listIterator();
	}

	/**
	 * {@inheritDoc}.
	 *
	 * Every attempt to modify the returned ListIterator will cause an
	 * {@link UnsupportedOperationException}
	 */
	public ListIterator<T> listIterator(int index) {
		return unmodifiableStackList.listIterator(index);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public int indexOf(Object elem) {
		return stack.indexOf(elem);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void insertElementAt(T obj, int index) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public int lastIndexOf(Object elem) {
		return unmodifiableStackList.lastIndexOf(elem);
	}

	/**
	 * {@inheritDoc}.
	 *
	 * Every attempt to modify the returned sublist will cause an
	 * {@link UnsupportedOperationException}
	 */
	public List<T> subList(int fromIndex, int toIndex) {
		return unmodifiableStackList.subList(fromIndex, toIndex);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public T set(int index, T element) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public T remove(int index) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/*
	 * Unmodifiable collection view on stack
	 */
	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public boolean add(T o) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public void clear() {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public boolean contains(Object o) {
		return unmodifiableStackList.contains(o);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public boolean containsAll(Collection<?> c) {
		return unmodifiableStackList.containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public boolean equals(Object o) {
		return unmodifiableStackList.equals(o);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public int hashCode() {
		return unmodifiableStackList.hashCode();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public boolean isEmpty() {
		return unmodifiableStackList.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public Iterator<T> iterator() {
		return unmodifiableStackList.iterator();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public boolean remove(Object o) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException
	 *             always, because this Stack is unmodifiable.
	 */
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public int size() {
		return unmodifiableStackList.size();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public Object[] toArray() {
		return unmodifiableStackList.toArray();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	public <TYPE> TYPE[] toArray(TYPE[] a) {
		return unmodifiableStackList.toArray(a);
	}

}
