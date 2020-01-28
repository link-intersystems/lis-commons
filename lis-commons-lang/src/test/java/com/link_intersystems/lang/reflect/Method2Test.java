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
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.expect;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.link_intersystems.lang.reflect.facadetestclasses.HigherInterface;
import com.link_intersystems.lang.reflect.facadetestclasses.HigherOverload;
import com.link_intersystems.lang.reflect.facadetestclasses.HigherReturn;
import com.link_intersystems.lang.reflect.facadetestclasses.HigherVisibility;
import com.link_intersystems.lang.reflect.facadetestclasses.LowerOverload;
import com.link_intersystems.lang.reflect.facadetestclasses.LowerReturn;
import com.link_intersystems.lang.reflect.facadetestclasses.LowerVisibility;
import com.link_intersystems.lang.reflect.facadetestclasses.LowerWithInterface;

public class Method2Test {

	@Test(expected = IllegalArgumentException.class)
	public void nullConstructorArg() {
		new Method2(null);
	}

	@Test
	public void privateMethod() throws SecurityException, NoSuchMethodException {
		Method2 higher = Class2.get(HigherVisibility.class)
				.getApplicableMethod("privateMethod");
		Method2 lower = Class2.get(LowerVisibility.class).getApplicableMethod(
				"privateMethod");
		boolean overridden = lower.overrides(higher);
		Assert.assertFalse(overridden);
	}

	@Test
	public void packageMethod() throws SecurityException, NoSuchMethodException {
		Method2 higher = Class2.get(HigherVisibility.class)
				.getApplicableMethod("packageMethod");
		Method2 lower = Class2.get(LowerVisibility.class).getApplicableMethod(
				"packageMethod");
		boolean overridden = lower.overrides(higher);
		Assert.assertTrue(overridden);
	}

	@Test
	public void protectedMethod() throws SecurityException,
			NoSuchMethodException {
		Method2 higher = Class2.get(HigherVisibility.class)
				.getApplicableMethod("protectedMethod");
		Method2 lower = Class2.get(LowerVisibility.class).getApplicableMethod(
				"protectedMethod");
		boolean overridden = lower.overrides(higher);
		Assert.assertTrue(overridden);
	}

	@Test
	public void publicMethod() throws SecurityException, NoSuchMethodException {
		Method2 higher = Class2.get(HigherVisibility.class)
				.getApplicableMethod("publicMethod");
		Method2 lower = Class2.get(LowerVisibility.class).getApplicableMethod(
				"publicMethod");
		boolean overridden = lower.overrides(higher);
		Assert.assertTrue(overridden);
	}

	@Test
	public void packageToPublic() throws SecurityException,
			NoSuchMethodException {
		Method2 higher = Class2.get(HigherVisibility.class)
				.getApplicableMethod("packageToPublic");
		Method2 lower = Class2.get(LowerVisibility.class).getApplicableMethod(
				"packageToPublic");
		boolean overridden = lower.overrides(higher);
		Assert.assertTrue(overridden);
	}

	@Test
	public void returnType() throws SecurityException, NoSuchMethodException {
		Method2 higher = Class2.get(HigherReturn.class).getApplicableMethod(
				"get");
		Method2 lower = Class2.get(LowerReturn.class)
				.getApplicableMethod("get");
		boolean overridden = lower.overrides(higher);
		Assert.assertTrue(overridden);
	}

	@Test
	public void interfaceMethodsDoNotOverride() throws SecurityException,
			NoSuchMethodException {
		Method2 higher = Class2.get(HigherInterface.class).getApplicableMethod(
				"get");
		Method2 lower = Class2.get(LowerWithInterface.class)
				.getApplicableMethod("get");
		boolean overridden = lower.overrides(higher);
		Assert.assertFalse(overridden);
	}

	@Test
	public void overloadsWithAssignableReturnTypes() throws SecurityException,
			NoSuchMethodException {
		Method2 higher = Class2.get(HigherOverload.class).getApplicableMethod(
				"get");
		Method2 lower = Class2.get(LowerOverload.class).getApplicableMethod(
				"get", Integer.TYPE);
		boolean overloaded = lower.overloads(higher);
		Assert.assertTrue(overloaded);
		overloaded = higher.overloads(lower);
		Assert.assertFalse(overloaded);
	}

	@Test
	public void getDefinition() throws SecurityException, NoSuchMethodException {
		Method2 arrayListGet = Class2.get(ArrayList.class).getApplicableMethod(
				"get", Integer.TYPE);
		Method definition = arrayListGet.getDefinition();
		Assert.assertNotNull(definition);
		Method declaredMethod = List.class.getDeclaredMethod("get",
				Integer.TYPE);
		Assert.assertEquals(declaredMethod, definition);
	}

	@Test
	public void getDefinitionIsTheActualMethod() throws SecurityException,
			NoSuchMethodException {
		Method2 isEmptyImpl = Class2.get(Collection.class).getApplicableMethod(
				"isEmpty");
		Method definition = isEmptyImpl.getDefinition();
		Assert.assertNotNull(definition);
		Method declaredMethod = Collection.class.getDeclaredMethod("isEmpty");
		Assert.assertEquals(declaredMethod, definition);
	}

	@Test
	public void overloads() throws SecurityException, NoSuchMethodException {
		Method2 higher = Class2.get(HigherOverload.class).getApplicableMethod(
				"getObj");
		Method2 lower = Class2.get(LowerOverload.class).getApplicableMethod(
				"getObj", Integer.TYPE);
		boolean overloaded = lower.overloads(higher);
		Assert.assertTrue(overloaded);
		overloaded = higher.overloads(lower);
		Assert.assertTrue(overloaded);
	}

	@Test
	public void invokeApplicable() throws Exception {
		Method2 arrayListAdd = Class2.get(ArrayList.class).getApplicableMethod(
				"add", Object.class);
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayListAdd.getInvokable(arrayList).invoke("Test");
		assertFalse(arrayList.isEmpty());
	}

	@SuppressWarnings("all")
	@Test(expected = IllegalArgumentException.class)
	public void invokeParamsMustNotBeNull() throws Exception {
		Method2 arrayListAdd = Class2.get(ArrayList.class).getApplicableMethod(
				"add", Object.class);
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayListAdd.getInvokable(arrayList).invoke(null);
		assertFalse(arrayList.isEmpty());
	}

	@Test
	public void invokeParamsContainsNull() throws Exception {
		Method2 arrayListAdd = Class2.get(ArrayList.class).getApplicableMethod(
				"add", Object.class);
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayListAdd.getInvokable(arrayList).invoke(new Object[] { null });
		assertFalse(arrayList.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void invokeNotApplicable() throws Exception {
		Method2 arrayListAdd = Class2.get(ArrayList.class).getApplicableMethod(
				"add", Object.class);
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayListAdd.getInvokable(arrayList).invoke(1, "Test");
	}

	@Test
	public void declaredExceptions() throws Exception {
		Method2 readMethod = Class2.get(InputStream.class).getApplicableMethod(
				"read");
		Class<?>[] declaredExceptionTypes = readMethod
				.getDeclaredExceptionTypes();
		assertNotNull(declaredExceptionTypes);
		assertEquals(1, declaredExceptionTypes.length);
		assertEquals(IOException.class, declaredExceptionTypes[0]);
	}

	@Test
	public void overriddenBy() throws Exception {
		Method2 readMethod = Class2.get(InputStream.class).getApplicableMethod(
				"read");
		Method2 overriddenReadMethod = Class2.get(FilterInputStream.class)
				.getApplicableMethod("read");
		assertTrue(readMethod.isOverriddenBy(overriddenReadMethod));
		assertFalse(overriddenReadMethod.isOverriddenBy(readMethod));
	}

	@Test
	public void overriddenByWithJavaMethod() throws Exception {
		Method2 readMethod = Class2.get(InputStream.class).getApplicableMethod(
				"read");
		Method2 overriddenReadMethod = Class2.get(FilterInputStream.class)
				.getApplicableMethod("read");
		assertTrue(readMethod.isOverriddenBy(overriddenReadMethod.getMember()));
		assertFalse(overriddenReadMethod.isOverriddenBy(readMethod));
	}

	@Test(expected = IllegalArgumentException.class)
	public void forMethodNull() throws Exception {
		Method2.forMethod(null);
	}

	@Test(expected = IllegalStateException.class)
	@PrepareOnlyThisForTest(Class2.class)
	public void forMethodImplementationError() throws Exception {
		Method readMethod = InputStream.class.getDeclaredMethod("read");
		PowerMock.mockStatic(Class2.class);
		Class2<?> class2Mock = PowerMock.createNiceMock(Class2.class);
		Class2.get(InputStream.class);
		PowerMock.expectLastCall().andReturn(class2Mock);
		expect(class2Mock.getMethod2(readMethod)).andThrow(
				new NoSuchMethodException());
		PowerMock.replay(Class2.class, class2Mock);

		Method2.forMethod(readMethod);
	}

}
