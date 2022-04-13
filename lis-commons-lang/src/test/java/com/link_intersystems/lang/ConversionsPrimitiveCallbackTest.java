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

import org.junit.jupiter.api.Test;

/**
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 */
class ConversionsPrimitiveCallbackTest  {

	@Test
	void booleanCallback() {
		Class<?> type = Boolean.TYPE;
		PrimitiveCallback assertion = new PrimitiveCallbackAssertion(type);
		Conversions.unbox(Boolean.valueOf(true), assertion);
	}

	@Test
	void byteCallback() {
		Class<?> type = Byte.TYPE;
		PrimitiveCallback assertion = new PrimitiveCallbackAssertion(type);
		Conversions.unbox(Byte.valueOf((byte) 13), assertion);
	}

	@Test
	void shortCallback() {
		Class<?> type = Short.TYPE;
		PrimitiveCallback assertion = new PrimitiveCallbackAssertion(type);
		Conversions.unbox(Short.valueOf((short) 13), assertion);
	}

	@Test
	void characterCallback() {
		Class<?> type = Character.TYPE;
		PrimitiveCallback assertion = new PrimitiveCallbackAssertion(type);
		Conversions.unbox(Character.valueOf('A'), assertion);
	}

	@Test
	void integerCallback() {
		Class<?> type = Integer.TYPE;
		PrimitiveCallback assertion = new PrimitiveCallbackAssertion(type);
		Conversions.unbox(Integer.valueOf(13), assertion);
	}

	@Test
	void longCallback() {
		Class<?> type = Long.TYPE;
		PrimitiveCallback assertion = new PrimitiveCallbackAssertion(type);
		Conversions.unbox(Long.valueOf(13), assertion);
	}

	@Test
	void floatCallback() {
		Class<?> type = Float.TYPE;
		PrimitiveCallback assertion = new PrimitiveCallbackAssertion(type);
		Conversions.unbox(Float.valueOf(13.13f), assertion);
	}

	@Test
	void doubleCallback() {
		Class<?> type = Double.TYPE;
		PrimitiveCallback assertion = new PrimitiveCallbackAssertion(type);
		Conversions.unbox(Double.valueOf(13.13), assertion);
	}

	private static class PrimitiveCallbackAssertion implements
			PrimitiveCallback {

		private final Class<?> primitiveType;

		public PrimitiveCallbackAssertion(Class<?> primitiveType) {
			this.primitiveType = primitiveType;
		}

		public void primitive(boolean value) {
			if (!boolean.class.equals(primitiveType)) {
				throw new AssertionError("wrong primitive type called back");
			}
		}

		public void primitive(byte value) {
			if (!byte.class.equals(primitiveType)) {
				throw new AssertionError("wrong primitive type called back");
			}
		}

		public void primitive(char value) {
			if (!char.class.equals(primitiveType)) {
				throw new AssertionError("wrong primitive type called back");
			}
		}

		public void primitive(short value) {
			if (!short.class.equals(primitiveType)) {
				throw new AssertionError("wrong primitive type called back");
			}
		}

		public void primitive(int value) {
			if (!int.class.equals(primitiveType)) {
				throw new AssertionError("wrong primitive type called back");
			}
		}

		public void primitive(long value) {
			if (!long.class.equals(primitiveType)) {
				throw new AssertionError("wrong primitive type called back");
			}
		}

		public void primitive(float value) {
			if (!float.class.equals(primitiveType)) {
				throw new AssertionError("wrong primitive type called back");
			}
		}

		public void primitive(double value) {
			if (!double.class.equals(primitiveType)) {
				throw new AssertionError("wrong primitive type called back");
			}
		}

	}
}
