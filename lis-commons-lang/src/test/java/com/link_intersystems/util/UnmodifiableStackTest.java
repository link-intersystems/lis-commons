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


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

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

	@Test(expected = UnsupportedOperationException.class)
	public void pop() {
		getUnmodifiableStack().pop();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void retainAll() {
		getUnmodifiableStack().retainAll(Arrays.asList("A"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void push() {
		getUnmodifiableStack().push(createComponentObject());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void removeAll() {
		getUnmodifiableStack().removeAll(Arrays.asList("A"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void remove() {
		getUnmodifiableStack().remove("A");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void sublistRemove() {
		getUnmodifiableStack().subList(0, 1).clear();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void clear() {
		getUnmodifiableStack().clear();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addAll() {
		getUnmodifiableStack().addAll(Arrays.asList("A"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void add() {
		getUnmodifiableStack().add("A");
	}

	@Test
	public void toStringTest() {
		String unmodifiableToString = getUnmodifiableStack().toString();
		String string = stack.toString();
		Assert.assertEquals(string, unmodifiableToString);
	}

	@Test
	public void search() {
		int position = getUnmodifiableStack().search("B");
		Assert.assertEquals(2, position);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void cloneStack() {
		Stack<Object> clone = (Stack<Object>) getUnmodifiableStack().clone();
		Assert.assertNotNull(clone);
		clone.equals(getUnmodifiableStack());

		Assert.assertTrue(clone instanceof UnmodifiableStack<?>);

	}

}
