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

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

public class Constructor2Test {

	@SuppressWarnings("rawtypes")
	@Test
	public void bestFitConstructor() throws SecurityException,
			NoSuchMethodException {
		Class2<TreeMap> classInfoImpl = Class2.get(TreeMap.class);
		Constructor2<TreeMap> constructorInfo = classInfoImpl
				.getApplicableConstructor(TreeMap.class);
		assertNotNull(constructorInfo);
		Constructor<?> selectedConstructor = constructorInfo.getMember();
		Constructor<TreeMap> constructor = TreeMap.class
				.getConstructor(SortedMap.class);
		assertEquals(constructor, selectedConstructor);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void bestFitConstructorByInvocationParamObjects()
			throws SecurityException, NoSuchMethodException {
		Class2<TreeMap> classInfoImpl = Class2.get(TreeMap.class);
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		Constructor2<TreeMap> constructorInfo = classInfoImpl
				.getApplicableConstructor(treeMap);
		assertNotNull(constructorInfo);
		Constructor<?> selectedConstructor = constructorInfo.getMember();
		Constructor<TreeMap> constructor = TreeMap.class
				.getConstructor(SortedMap.class);
		assertEquals(constructor, selectedConstructor);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void constructorSelected() throws SecurityException,
			NoSuchMethodException {
		Class2<TreeMap> classInfoImpl = Class2.get(TreeMap.class);
		Constructor2<TreeMap> constructorInfo = classInfoImpl
				.getApplicableConstructor(HashMap.class);
		assertNotNull(constructorInfo);
		Constructor<?> selectedConstructor = constructorInfo.getMember();
		assertNotNull(selectedConstructor);
		Constructor<TreeMap> constructor = TreeMap.class
				.getConstructor(Map.class);
		assertEquals(constructor, selectedConstructor);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void invoke() throws Exception {
		Class2<TreeMap> classInfoImpl = Class2.get(TreeMap.class);
		Constructor2<TreeMap> constructor2 = classInfoImpl
				.getApplicableConstructor();
		assertNotNull(constructor2);
		TreeMap invoke = constructor2.getInvokable().invoke();
		assertNotNull(invoke);

	}

	@Test
	public void declaredExceptions() throws Exception {
		Class2<URL> classInfoImpl = Class2.get(URL.class);
		Constructor2<URL> constructor2 = classInfoImpl
				.getApplicableConstructor(String.class);
		assertNotNull(constructor2);
		Class<?>[] declaredExceptionTypes = constructor2
				.getDeclaredExceptionTypes();
		assertEquals(1, declaredExceptionTypes.length);
		assertEquals(MalformedURLException.class, declaredExceptionTypes[0]);
	}

}
