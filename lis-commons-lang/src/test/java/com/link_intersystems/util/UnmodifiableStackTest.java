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


import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;
import java.util.Vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnmodifiableStackTest extends UnmodifiableVectorTest {

	private Stack<Object> stack;

	private Stack<Object> unmodifiableStack;

	@Override
	protected final Vector<Object> getUnmodifiableVector() {
		return getUnmodifiableStack();
	}

	@Override
	protected Object createComponentObject() {
		return new String("D");
	}

	protected Stack<Object> getUnmodifiableStack() {
		if (stack == null) {
			stack = new Stack<Object>();
			stack.push("A");
			stack.push("B");
			stack.push("C");
			unmodifiableStack = UtilFacade.unmodifiableStack(stack);
		}
		return unmodifiableStack;
	}

	@Override
	protected Object getCollectionObject() {
		return stack.peek();
	}

	@Override
	protected Collection<Object> getCollectionObjects() {
		Object peek = stack.peek();
		return Arrays.asList(new Object[] { peek });
	}

	@Test
	public void hashCodeTest() {
		int hashCode = getUnmodifiableStack().hashCode();
		int hashCode2 = stack.hashCode();
		assertEquals(hashCode2, hashCode);
	}

	@Test
	public void empty() {
		getUnmodifiableStack().empty();
	}

	@Test
	public void peek() {
		getUnmodifiableStack().peek();
	}

	@Test
	public void pop() {
		assertThrows(UnsupportedOperationException.class, () ->getUnmodifiableStack().pop());
	}

	@Test
	public void retainAll() {
		assertThrows(UnsupportedOperationException.class, () ->getUnmodifiableStack().retainAll(Arrays.asList("A")));
	}

	@Test
	public void push() {
		assertThrows(UnsupportedOperationException.class, () ->	getUnmodifiableStack().push(createComponentObject()));
	}

	@Test
	public void removeAll() {
		assertThrows(UnsupportedOperationException.class, () ->getUnmodifiableStack().removeAll(Arrays.asList("A")));
	}

	@Test
	public void remove() {
		assertThrows(UnsupportedOperationException.class, () ->getUnmodifiableStack().remove("A"));
	}

	@Test
	public void sublistRemove() {
		assertThrows(UnsupportedOperationException.class, () ->getUnmodifiableStack().subList(0, 1).clear());
	}

	@Test
	public void clear() {
		assertThrows(UnsupportedOperationException.class, () ->getUnmodifiableStack().clear());
	}

	@Test
	public void addAll() {
		assertThrows(UnsupportedOperationException.class, () ->getUnmodifiableStack().addAll(Arrays.asList("A")));
	}

	@Test
	public void add() {
		assertThrows(UnsupportedOperationException.class, () ->getUnmodifiableStack().add("A"));
	}

	@Test
	public void toStringTest() {
		String unmodifiableToString = getUnmodifiableStack().toString();
		String string = stack.toString();
		assertEquals(string, unmodifiableToString);
	}

	@Test
	public void search() {
		int position = getUnmodifiableStack().search("B");
		assertEquals(2, position);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void cloneStack() {
		Stack<Object> clone = (Stack<Object>) getUnmodifiableStack().clone();
		Assertions.assertNotNull(clone);
		clone.equals(getUnmodifiableStack());

		assertTrue(clone instanceof UnmodifiableStack<?>);

	}

}
