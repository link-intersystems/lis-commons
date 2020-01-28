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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JMenu;

import org.junit.Test;

import com.link_intersystems.util.SerializableTemplateObjectFactory;

public class SignaturePredicateTest {

	@Test(expected = IllegalArgumentException.class)
	public void constructorSignatureWithNull() {
		new SignaturePredicate((Constructor<?>) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void methodSignatureWithNull() {
		new SignaturePredicate((Method) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invokableSignatureWithNull() {
		new SignaturePredicate((Member2<?>) null);
	}

	@Test
	public void methodEqual() throws SecurityException, NoSuchMethodException {
		Method declaredMethod = Collection.class.getDeclaredMethod("add",
				Object.class);
		Method declaredMethod2 = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		SignaturePredicate signaturePredicate = new SignaturePredicate(
				declaredMethod);
		boolean evaluate = signaturePredicate.evaluate(declaredMethod2);
		assertTrue(evaluate);
	}

	@Test
	public void methodEqualWithInvokable() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = Collection.class.getDeclaredMethod("add",
				Object.class);
		Method declaredMethod2 = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		SignaturePredicate signaturePredicate = new SignaturePredicate(
				declaredMethod);
		boolean evaluate = signaturePredicate.evaluate(new Method2(
				declaredMethod2));
		assertTrue(evaluate);
	}

	@Test
	public void methodEqualAfterDeserialization() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = Collection.class.getDeclaredMethod("add",
				Object.class);
		Method declaredMethod2 = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		SignaturePredicate signaturePredicate = new SignaturePredicate(
				declaredMethod);
		SerializableTemplateObjectFactory<SignaturePredicate> serializableTemplateObjectFactory = new SerializableTemplateObjectFactory<SignaturePredicate>(
				signaturePredicate);
		SignaturePredicate object = serializableTemplateObjectFactory
				.getObject();
		boolean evaluate = object.evaluate(new Method2(declaredMethod2));
		assertTrue(evaluate);
	}

	@Test
	public void methodNotEqual() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = JMenu.class.getDeclaredMethod("add",
				String.class);
		Method declaredMethod2 = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		SignaturePredicate signaturePredicate = new SignaturePredicate(
				declaredMethod);
		boolean evaluate = signaturePredicate.evaluate(declaredMethod2);
		assertFalse(evaluate);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void methodComparedWithConstructor() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		Constructor<ArrayList> declaredConstructor = ArrayList.class
				.getDeclaredConstructor();
		SignaturePredicate signaturePredicate = new SignaturePredicate(
				declaredMethod);
		boolean evaluate = signaturePredicate.evaluate(declaredConstructor);
		assertFalse(evaluate);
	}

	@Test(expected = IllegalArgumentException.class)
	public void evaluateAgainstAField() throws SecurityException,
			NoSuchMethodException, NoSuchFieldException {
		Method declaredMethod = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		Field field = JComponent.class.getDeclaredField("WHEN_FOCUSED");
		SignaturePredicate signaturePredicate = new SignaturePredicate(
				declaredMethod);
		boolean evaluate = signaturePredicate.evaluate(field);
		assertFalse(evaluate);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void evaluateWithAnotherSignature() throws SecurityException,
			NoSuchMethodException {
		Constructor<ArrayList> declaredConstructor = ArrayList.class
				.getDeclaredConstructor(int.class);
		Class2<ArrayList> class2 = Class2.get(ArrayList.class);
		Constructor2<ArrayList> applicableConstructor = class2
				.getApplicableConstructor(int.class);
		SignaturePredicate signaturePredicate = new SignaturePredicate(
				declaredConstructor);
		boolean evaluate = signaturePredicate.evaluate(applicableConstructor
				.getSignature());
		assertTrue(evaluate);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void evaluateWithUnsupportedObject() throws SecurityException,
			NoSuchMethodException {
		Constructor<ArrayList> declaredConstructor = ArrayList.class
				.getDeclaredConstructor(int.class);
		SignaturePredicate signaturePredicate = new SignaturePredicate(
				declaredConstructor);
		boolean evaluate = signaturePredicate.evaluate(Integer.valueOf(1));
		assertFalse(evaluate);
	}
}
