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

import org.junit.Test;

/**
 * Tests the {@link Conversions} primitive widening as defined by the java
 * language specification 5.1.2 Widening Primitive Conversion
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 */
public class ConversionsPrimitiveWideningTest {

	@Test(expected = IllegalArgumentException.class)
	public void nonPrimitiveFrom() {
		Conversions.isPrimitiveWidening(Byte.class, Double.TYPE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nonPrimitiveTo() {
		Conversions.isPrimitiveWidening(Byte.TYPE, Double.class);
	}

	/**
	 * byte to short, int, long, float, or double
	 */
	@Test
	public void byteWidening() {
		Class<?> from = Byte.TYPE;
		assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Short.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Integer.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Long.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
	}

	/**
	 * short to int, long, float, or double
	 */
	@Test
	public void shortWidening() {
		Class<?> from = Short.TYPE;
		assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Integer.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Long.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
	}

	/**
	 * char to int, long, float, or double
	 */
	@Test
	public void characterWidening() {
		Class<?> from = Character.TYPE;
		assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Integer.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Long.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
	}

	/**
	 * int to long, float, or double
	 */
	@Test
	public void integerWidening() {
		Class<?> from = Integer.TYPE;
		assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Integer.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Long.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
	}

	/**
	 * long to float or double
	 */
	@Test
	public void longWidening() {
		Class<?> from = Long.TYPE;
		assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Integer.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Long.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
	}

	/**
	 * float to double
	 */
	@Test
	public void floatWidening() {
		Class<?> from = Float.TYPE;
		assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Integer.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Long.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Float.TYPE));
		assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
	}

	/**
	 * double widening is not supported because it is the biggest primitive
	 * type.
	 */
	@Test
	public void doubleWidening() {
		Class<?> from = Double.TYPE;
		assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Integer.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Long.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Float.TYPE));
		assertFalse(Conversions.isPrimitiveWidening(from, Double.TYPE));
	}
}
