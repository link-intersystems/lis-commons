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

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.link_intersystems.lang.reflect.test_Member2Applicability.GenericMethod;
import com.link_intersystems.lang.reflect.test_Member2Applicability.GenericMethod.IntLongMap;
import com.link_intersystems.lang.reflect.test_Member2Applicability.GenericMethod.IntegerArray;
import com.link_intersystems.lang.reflect.test_Member2Applicability.GenericMethod.ListStringArray;
import com.link_intersystems.lang.reflect.test_Member2Applicability.GenericMethod.StringArray;
import com.link_intersystems.lang.reflect.test_Member2Applicability.VarargsMethod;

public class Member2ApplicabilityTest {

	private Method genericMethod;
	private Method2 genericMethod2;
	private Method noVarargMethod;
	private Method2 noVarargMethod2;
	private Method varargMethod;
	private Method2 varargMethod2;
	private Method anotherGenericMethod;
	private Method2 anotherGenericMethod2;
	private Method stringMethod;
	private Method2 stringMethod2;

	@Before
	public void setup() throws SecurityException, NoSuchMethodException {
		Method[] declaredMethods = GenericMethod.class.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getName().equals("genericMethod")) {
				genericMethod = method;
			}
			if (method.getName().equals("anotherGenericMethod")) {
				anotherGenericMethod = method;
			}
		}
		Assert.assertNotNull(anotherGenericMethod);
		Assert.assertNotNull(genericMethod);
		genericMethod2 = new Method2(genericMethod);
		anotherGenericMethod2 = new Method2(anotherGenericMethod);

		noVarargMethod = VarargsMethod.class.getDeclaredMethod("method",
				int.class, int.class, int.class);
		noVarargMethod2 = new Method2(noVarargMethod);

		stringMethod = VarargsMethod.class.getDeclaredMethod("string",
				String.class, String[].class);
		stringMethod2 = new Method2(stringMethod);

		varargMethod = VarargsMethod.class.getDeclaredMethod("method",
				int.class, int.class, int[].class);
		varargMethod2 = new Method2(varargMethod);
		boolean varArgs = varargMethod2.isVarArgs();
		Assert.assertTrue(varArgs);
	}

	@Test
	public void genericTypeArguments() {
		/*
		 * This call is to ensure that the test is consistent. E.g. if the
		 * StringArray or the genericMethod definition changes this call must
		 * still compile. If it does we are sure that we test the right thing.
		 * Otherwise the compiler errors will give us a hint what changed.
		 */
		GenericMethod genericMethod = new GenericMethod();
		StringArray stringArray = new StringArray();
		IntLongMap aMap = new IntLongMap();
		genericMethod.genericMethod(stringArray, aMap);

		Member2Applicability member2Applicability = new Member2Applicability(
				genericMethod2);
		boolean applicable = member2Applicability.isApplicable(new Class<?>[] {
				StringArray.class, IntLongMap.class });
		Assert.assertTrue(applicable);

		applicable = member2Applicability.isApplicable(new Class<?>[] {
				IntegerArray.class, IntLongMap.class });
		Assert.assertTrue(applicable);

	}

	@Test
	public void anotherGenericMethod() {
		/*
		 * This call is to ensure that the test is consistent. E.g. if the
		 * StringArray or the genericMethod definition changes this call must
		 * still compile. If it does we are sure that we test the right thing.
		 * Otherwise the compiler errors will give us a hint what changed.
		 */
		GenericMethod genericMethod = new GenericMethod();
		genericMethod.anotherGenericMethod(new ListStringArray(),
				new IntLongMap());

		Member2Applicability member2Applicability = new Member2Applicability(
				anotherGenericMethod2);
		boolean applicable = member2Applicability.isApplicable(new Class<?>[] {
				ListStringArray.class, IntLongMap.class });
		Assert.assertTrue(applicable);

	}

	@Test
	public void varargsMethod() {
		/*
		 * This call is to ensure that the test is consistent.
		 */
		VarargsMethod varargsMethod = new VarargsMethod();
		boolean varargs = varargsMethod.method(1, 2, 3);
		Assert.assertFalse(varargs);
		varargs = varargsMethod.method(1, 2, 3, 4);
		Assert.assertTrue(varargs);
		varargs = varargsMethod.method(1, 2);
		Assert.assertFalse(varargs);

		Member2Applicability member2Applicability = new Member2Applicability(
				varargMethod2);

		boolean varargsApplicable = member2Applicability
				.isApplicable(new Class<?>[] { int.class, int.class, int.class,
						int.class });
		Assert.assertTrue(varargsApplicable);

		varargsApplicable = member2Applicability.isApplicable(new Class<?>[] {
				int.class, int.class });
		Assert.assertTrue(varargsApplicable);

	}

	@Test
	public void varargsMethodWithEmptyVarArgs() {
		VarargsMethod varargsMethod = new VarargsMethod();
		varargsMethod.string("");

		Member2Applicability member2Applicability = new Member2Applicability(
				stringMethod2);

		boolean varargsApplicable = member2Applicability
				.isApplicable(new Class<?>[] { String.class });
		Assert.assertTrue(varargsApplicable);

		varargsApplicable = member2Applicability
				.isApplicable(new Class<?>[] {});
		Assert.assertFalse(varargsApplicable);

	}

	@Test
	public void noVarargsMethod() {
		/*
		 * This call is to ensure that the test is consistent.
		 */
		VarargsMethod varargsMethod = new VarargsMethod();
		boolean varargs = varargsMethod.method(1, 2, 3);
		Assert.assertFalse(varargs);
		varargs = varargsMethod.method(1, 2, 3, 4);
		Assert.assertTrue(varargs);

		Member2Applicability member2Applicability = new Member2Applicability(
				noVarargMethod2);

		boolean varargsApplicable = member2Applicability
				.isApplicable(new Class<?>[] { int.class, int.class, int.class,
						int.class });
		Assert.assertFalse(varargsApplicable);

	}

	@Test
	public void autoboxing() {
		/*
		 * This call is to ensure that the test is consistent.
		 */
		VarargsMethod varargsMethod = new VarargsMethod();
		boolean varargs = varargsMethod.method(1, 2, 3);
		Assert.assertFalse(varargs);
		varargs = varargsMethod.method(1, 2, 3, 4);
		Assert.assertTrue(varargs);
		varargs = varargsMethod.method(1, 2);
		Assert.assertFalse(varargs);

		Member2Applicability member2Applicability = new Member2Applicability(
				noVarargMethod2);

		boolean varargsApplicable = member2Applicability
				.isApplicable(new Class<?>[] { Integer.class, Integer.class,
						Integer.class });
		Assert.assertTrue(varargsApplicable);
	}

}
