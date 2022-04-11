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
package com.link_intersystems.lang.reflect;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

public class MethodInvokingTransformerTest {

	private InvocationArgumentsResolver argumentResolver;
	private Method charAtMethod;

	@Before
	public void setup() throws NoSuchMethodException {
		argumentResolver = new InvocationArgumentsResolver() {

			public Object[] getArguments(Object invokedObject,
					Method invokedMethod) {
				return new Object[] { 1 };
			}
		};
		charAtMethod = String.class.getDeclaredMethod("charAt", Integer.TYPE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transformWrongType() {
		Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod,
				argumentResolver);
		Character valueOf = Character.valueOf('c');
		transformer.apply(valueOf);
	}

	@Test(expected = NullPointerException.class)
	public void transformNullValue() {
		Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod,
				argumentResolver);
		transformer.apply(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongArguments() {
		Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod,
				new InvocationArgumentsResolver() {

					public Object[] getArguments(Object invokedObject,
							Method invokedMethod) {
						return new Object[] { "A" };
					}
				});
		transformer.apply("Test");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTypeConstructor() {
		new MethodInvokingTransformer((Method2) null, argumentResolver);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullArgumentResolverConstructor() {
		new MethodInvokingTransformer(charAtMethod, null);
	}

	@Test
	public void invokeMethod() {
		String testString = "HelloWorld";

		Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod,
				argumentResolver);
		Object transform = transformer.apply(testString);
		assertNotNull(transform);
		assertTrue(transform instanceof Character);
		Character charAt1 = (Character) transform;
		assertEquals('e', charAt1.charValue());
	}

	@Test(expected = NullPointerException.class)
	public void invokeInstanceMethodOnNull() {
		Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod,
				argumentResolver);
		transformer.apply(null);
	}

	@Test
	public void invokeMethodWithNoArguments() throws NoSuchMethodException {
		String testString = "HelloWorld";
		Method toStringMethod = Object.class.getDeclaredMethod("toString");
		Function<Object, Object> transformer = new MethodInvokingTransformer(toStringMethod);
		Object transform = transformer.apply(testString);
		assertNotNull(transform);
		assertTrue(transform instanceof String);
		String string = (String) transform;
		assertEquals("HelloWorld", string);
	}

	@Test(expected = IllegalStateException.class)
	public void invokeInstanceMethodThrowsUnknownException() throws NoSuchMethodException {
		String testString = "HelloWorld";
		Method toStringMethod = Object.class.getDeclaredMethod("toString");
		Function<Object, Object> transformer = new MethodInvokingTransformer(toStringMethod) {

			@Override
			protected Object invokeInstanceMethod(Object targetObject)
					throws Exception {
				throw new IOException();
			}

		};
		transformer.apply(testString);
	}

	@Test
	public void invokeStaticMethodWithNullTarget() throws SecurityException,
			NoSuchMethodException {
		InvocationArgumentsResolver invocationArgumentsResolver = createNiceMock(InvocationArgumentsResolver.class);
		Method formatMethod = String.class.getDeclaredMethod("format",
				String.class, Object[].class);
		expect(
				invocationArgumentsResolver.getArguments(String.class,
						formatMethod)).andReturn(
				new Object[] { "Hello %s", "World" });
		replay(invocationArgumentsResolver);

		Function<Object, Object> transformer = new MethodInvokingTransformer(formatMethod,
				invocationArgumentsResolver);
		Object transform = transformer.apply(null);
		assertNotNull(transform);
		assertEquals("Hello World", transform);
	}

	@Test
	public void invokeStaticMethodWithClassTarget() throws SecurityException,
			NoSuchMethodException {
		InvocationArgumentsResolver invocationArgumentsResolver = createNiceMock(InvocationArgumentsResolver.class);
		Method formatMethod = String.class.getDeclaredMethod("format",
				String.class, Object[].class);
		expect(
				invocationArgumentsResolver.getArguments(String.class,
						formatMethod)).andReturn(
				new Object[] { "Hello %s", "World" });
		replay(invocationArgumentsResolver);

		Function<Object, Object> transformer = new MethodInvokingTransformer(formatMethod,
				invocationArgumentsResolver);
		Object transform = transformer.apply(String.class);
		assertNotNull(transform);
		assertEquals("Hello World", transform);
	}

}
