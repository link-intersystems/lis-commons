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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

public class Class2ApplicableTest {

	@Test
	public void applicableVarargsMethod() throws Exception {
		Class2<?> classInfo = Class2.get(Class2ApplicableTestClass.class);
		Method2 applicableMethod = classInfo.getApplicableMethod("method",
				Object.class, String.class, int.class, int.class);
		assertNotNull(applicableMethod);
		assertTrue(applicableMethod.isVarArgs());
		Class2ApplicableTestClass invokable = new Class2ApplicableTestClass();
		String result = applicableMethod.getInvokable(invokable).invoke(
				"An object", "A string", 44, 33);
		assertEquals("varargs", result);
		applicableMethod = classInfo.getApplicableMethod("method",
				Object.class, String.class, int.class);
		assertNotNull(applicableMethod);
		assertFalse("No vararg method should be applicable",
				applicableMethod.isVarArgs());
		result = applicableMethod.getInvokable(invokable).invoke("An object",
				"A string", 44);
		assertEquals("fixed", result);
		
		invokable.method("", "");
		applicableMethod = classInfo.getApplicableMethod("method",
				Object.class, String.class);
		assertNotNull(applicableMethod);
	}

	@Test
	public void applicableVarargsMethodWithAccessType()
			throws SecurityException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Class2<?> classInfo = Class2
				.get(Class2ApplicableTestClassWithPrivateMethod.class);
		Method2 applicableMethod = classInfo.getApplicableMethod("method",
				new AccessType[] { AccessType.PUBLIC }, Object.class,
				String.class, int.class, int.class);
		assertNull(applicableMethod);

		applicableMethod = classInfo.getApplicableMethod("method",
				new AccessType[] { AccessType.PRIVATE }, Object.class,
				String.class, int.class, int.class);
		assertNotNull(applicableMethod);
	}

	@Test
	public void selectMostSpecific() throws SecurityException,
			NoSuchMethodException {
		Class2<?> class2Impl = Class2
				.get(ClassWithSeveralSpecificMethods.class);
		Method2 MethodInvokable = class2Impl.getApplicableMethod("out",
				new Class<?>[] { null });
		assertNotNull(MethodInvokable);
		Method method = MethodInvokable.getMember();
		Method shouldBeSelected = ClassWithSeveralSpecificMethods.class
				.getDeclaredMethod("out", String.class);
		assertEquals(shouldBeSelected, method);
	}

}

class ClassWithSeveralSpecificMethods {

	public void out(Object charSequence) {

	}

	public void out(CharSequence charSequence) {

	}

	public void out(String charSequence) {

	}

}

class SomeTestClass {

	public SomeTestClass(Map<?, ?> map, SortedMap<?, ?> sortedMap) {
	}

	public SomeTestClass(SortedMap<?, ?> sortedMap, Map<?, ?> map) {
	}
}

class Class2ApplicableTestClass {

	public String method(Object o, String s, int... vars) {
		return "varargs";
	}

	public String method(Object o, String s, int var) {
		return "fixed";
	}

}

class Class2ApplicableTestClassWithPrivateMethod {

	@SuppressWarnings("unused")
	private String method(Object o, String s, int... vars) {
		return "varargs";
	}

	public String method(Object o, String s, int var) {
		return "fixed";
	}

}
