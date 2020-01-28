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
package com.link_intersystems.lang;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

/**
 * Tests the {@link Conversions} narrowing reference conversion as defined by
 * the java language specification 5.1.6 Narrowing Reference Conversions
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 */
public class ConversionsReferenceNarrowingTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullFrom() {
		Class<?> s = null;
		Class<?> t = Collection.class;
		Conversions.isNarrowingReference(s, t);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTo() {
		Class<?> s = List.class;
		Class<?> t = null;
		Conversions.isNarrowingReference(s, t);
	}

	/**
	 * From any reference type S to any reference type T, provided that S is a
	 * proper supertype (§4.10) of T. (An important special case is that there
	 * is a narrowing conversion from the class type Object to any other
	 * reference type.)
	 */
	@Test
	public void supertype() {
		Class<?> s = Collection.class;
		Class<?> t = List.class;
		boolean narrowingReference = false;
		narrowingReference = Conversions.isNarrowingReference(s, t);
		assertTrue(narrowingReference);
		narrowingReference = Conversions.isNarrowingReference(t, s);
		assertFalse(narrowingReference);
	}

	/**
	 * From any class type C to any non-parameterized interface type K, provided
	 * that C is not final and does not implement K.
	 */
	@Test
	public void notFinalClassDoesntImplementInterface() {
		Class<?> c = Thread.class;
		Class<?> k = Appendable.class;

		assertFalse(Modifier.isFinal(c.getModifiers()));
		Type genericSuperclass = c.getGenericSuperclass();
		assertFalse(genericSuperclass instanceof ParameterizedType);

		boolean narrowingReference = false;
		narrowingReference = Conversions.isNarrowingReference(c, k);
		assertTrue(narrowingReference);
	}

	/**
	 * From any interface type J to any non-parameterized class type C that is
	 * not final.
	 */
	@Test
	public void interfaceToNonFinalClass() {
		Class<?> j = Appendable.class;
		Class<?> c = Thread.class;

		assertTrue(j.isInterface());
		Type genericSuperclass = c.getGenericSuperclass();
		assertFalse(genericSuperclass instanceof ParameterizedType);
		assertFalse(Modifier.isFinal(c.getModifiers()));

		boolean narrowingReference = false;
		narrowingReference = Conversions.isNarrowingReference(j, c);
		assertTrue(narrowingReference);
	}

	/**
	 * From the interface types Cloneable and java.io.Serializable to any array
	 * type T[].
	 */
	@Test
	public void interfaceClonableAndSerializableToAnyArray() {
		Class<?> c = Cloneable.class;
		Class<?> s = Serializable.class;
		Class<?> a = Thread[].class;

		assertTrue(a.isArray());

		boolean narrowingReference = false;
		narrowingReference = Conversions.isNarrowingReference(c, a);
		assertTrue(narrowingReference);
		narrowingReference = Conversions.isNarrowingReference(s, a);
		assertTrue(narrowingReference);
	}

	/**
	 * From any interface type J to any non-parameterized interface type K,
	 * provided that J is not a subinterface of K.
	 */
	@Test
	public void interfaceToNonParameterizedInterface() {
		Class<?> j = Appendable.class;
		Class<?> k = CharSequence.class;

		assertTrue(j.isInterface());
		assertTrue(k.isInterface());
		Type genericSuperclass = k.getGenericSuperclass();
		assertFalse(genericSuperclass instanceof ParameterizedType);
		assertFalse(k.isAssignableFrom(j));

		boolean narrowingReference = false;
		narrowingReference = Conversions.isNarrowingReference(j, k);
		assertTrue(narrowingReference);
	}

	/**
	 * From any array type SC[] to any array type TC[], provided that SC and TC
	 * are reference types and there is a narrowing conversion from SC to TC.
	 */
	@Test
	public void arrayNarrowing() {
		Class<?> sc = Appendable[].class;
		Class<?> tc = CharSequence[].class;

		assertTrue(sc.isArray());
		assertTrue(tc.isArray());

		boolean narrowingReference = false;
		narrowingReference = Conversions.isNarrowingReference(sc, tc);
		assertTrue(narrowingReference);
	}

}
