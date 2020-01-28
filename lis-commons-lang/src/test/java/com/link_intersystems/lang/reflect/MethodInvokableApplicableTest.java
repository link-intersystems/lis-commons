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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MethodInvokableApplicableTest {

	@Before
	public void setup() {
	}

	@Test
	public void applicableVarargs() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = MethodInfoApplicableTestClass.class
				.getDeclaredMethod("intVarargs", new Class<?>[] { Object.class,
						String.class, int[].class });
		Method2 methodInfo = new Method2(declaredMethod);
		Class<?>[] noVarargs = new Class[] { Object.class, String.class, null };
		boolean applicable = methodInfo.isApplicable(noVarargs);
		assertTrue(applicable);

		Class<?>[] equalVarargs = new Class[] { Object.class, String.class,
				int.class };
		applicable = methodInfo.isApplicable(equalVarargs);
		assertTrue(applicable);

		Class<?>[] varargs = new Class[] { Object.class, String.class,
				int.class, int.class };
		applicable = methodInfo.isApplicable(varargs);
		assertTrue(applicable);

		Class<?>[] notApplicableVarargs = new Class[] { Object.class,
				String.class };
		
		MethodInfoApplicableTestClass methodInfoApplicableTestClass = new MethodInfoApplicableTestClass();
		methodInfoApplicableTestClass.intVarargs("", "");
		applicable = methodInfo.isApplicable(notApplicableVarargs);
		assertTrue(applicable);
	}

	@Test
	public void fixedParameterLength() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = MethodInfoApplicableTestClass.class
				.getDeclaredMethod("fixed", new Class<?>[] { Object.class,
						String.class, int.class });
		Method2 methodInfo = new Method2(declaredMethod);
		Class<?>[] args = new Class[] { Object.class, String.class, int.class };
		boolean applicable = methodInfo.isApplicable(args);
		assertTrue(applicable);

		Class<?>[] lessArgs = new Class[] { Object.class, String.class,
				int.class, int.class };
		applicable = methodInfo.isApplicable(lessArgs);
		assertFalse(applicable);
	}

	@Test
	public void genericMethodParams() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = MethodInfoApplicableTestClass.class
				.getDeclaredMethod("generic", new Class<?>[] {
						Collection.class, List.class, int.class });
		Method2 methodInfo = new Method2(declaredMethod);

		Class<?>[] args = new Class[] { Collection.class, List.class, int.class };
		boolean applicable = methodInfo.isApplicable(args);
		assertTrue(applicable);

		/*
		 * a map has more formal type parameters than defined in the methods
		 * parameter type. So the isApplicable method should fail fast.
		 */
		args = new Class[] { Map.class, List.class, int.class };
		applicable = methodInfo.isApplicable(args);
		assertFalse(applicable);

	}

	@Test
	public void autoboxingParams() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = MethodInfoApplicableTestClass.class
				.getDeclaredMethod("fixed", new Class<?>[] { Object.class,
						String.class, int.class });
		Method2 methodInfo = new Method2(declaredMethod);

		Class<?>[] args = new Class[] { Object.class, String.class,
				Integer.class };
		boolean applicable = methodInfo.isApplicable(args);
		assertTrue(applicable);

	}
}

class MethodInfoApplicableTestClass {

	public void intVarargs(Object o, String s, int... vars) {

	}

	public void fixed(Object o, String s, int var) {

	}

	public <T, C> void generic(Collection<Object> o, List<String> s, int var) {

	}
}
