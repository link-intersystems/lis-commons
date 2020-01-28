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

import java.lang.reflect.Method;

import org.junit.Test;

public class NullInvocationArgumentsResolverTest {

	@Test
	public void defaults() throws SecurityException, NoSuchMethodException {
		Method primitiveTestMethod = NullInvocationArgumentsResolverTest.class
				.getDeclaredMethod("primitiveTestMethod", byte.class,
						short.class, char.class, int.class, long.class,
						float.class, double.class, boolean.class, Object.class);
		InvocationArgumentsResolver resolver = NullInvocationArgumentsResolver.INSTANCE;
		Object[] arguments = resolver.getArguments(null, primitiveTestMethod);
		assertEquals(9, arguments.length);

		assertEquals((byte) 0, arguments[0]);
		assertEquals((short) 0, arguments[1]);
		assertEquals((char) 0, arguments[2]);
		assertEquals(0, arguments[3]);
		assertEquals(0L, arguments[4]);
		assertEquals(0.0F, arguments[5]);
		assertEquals(0.0, arguments[6]);
		assertEquals(false, arguments[7]);
		assertEquals(null, arguments[8]);
	}

	static void primitiveTestMethod(byte b, short s, char c, int i, long l,
			float f, double d, boolean bool, Object o) {

	}
}
