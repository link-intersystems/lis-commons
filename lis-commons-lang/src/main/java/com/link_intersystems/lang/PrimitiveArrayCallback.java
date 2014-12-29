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

import java.lang.reflect.Array;


/**
 * A {@link PrimitiveCallback} that uses an object array to store the callback's
 * value in.
 * <p>
 * If one of the {@link PrimitiveCallback} methods is called on this
 * {@link PrimitiveArrayCallback}, it stores the callback's value in the backed
 * primitive array at the current position that this
 * {@link PrimitiveArrayCallback} is pointing at.
 * </p>
 * <p>
 * <strong>example</strong>
 *
 * <pre>
 * int[] arr = new int[10];
 * PrimitiveArrayCallback callback = new PrimitiveArrayCallback(arr);
 * callback.setIndex(8);
 * callback.primitive(13);
 * assertEquals(13, arr[8]);
 * </pre>
 *
 * </p>
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @since 1.0.0.0
 *
 */
public class PrimitiveArrayCallback implements PrimitiveCallback {

	private final Object primitiveArray;

	private boolean autoincrementEnabled = true;

	private int index = 0;

	private int length;

	/**
	 * Constructs a {@link PrimitiveArrayCallback} backed by the primitiveArray
	 * with a start index of 0.
	 *
	 * @param primitiveArray
	 *            the primitiveArray that is used to store the callback value
	 *            in.
	 *
	 * @since 1.0.0.0
	 */
	public PrimitiveArrayCallback(Object primitiveArray) {
		Assert.notNull("primitiveArray", primitiveArray);

		Class<?> primitiveArrayClass = primitiveArray.getClass();
		boolean isArrayClass = primitiveArrayClass.isArray();
		Assert.isTrue(isArrayClass, "primitiveArray must not be an array");

		Class<?> primitiveComponentType = primitiveArrayClass
				.getComponentType();
		boolean isPrimitive = primitiveComponentType.isPrimitive();
		Assert.isTrue(
				isPrimitive,
				"primitiveArray's component type must be a primitive type, but was %s",
				primitiveComponentType);

		this.primitiveArray = primitiveArray;
		length = Array.getLength(primitiveArray);
	}

	/**
	 * Sets the current index this {@link PrimitiveArrayCallback} is pointing at
	 * for storing callback values to the index.
	 *
	 * @param index
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of the bounds of the backed array.
	 *
	 * @since 1.0.0.0
	 */
	public void setIndex(int index) {
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("index " + index
					+ " is out of the backed primitive array's bounds 0 - "
					+ length);
		}
		this.index = index;
	}

	private void incrementIfEnabled() {
		if (isAutoincrementEnabled() && index < (length - 1)) {
			index++;
		}
	}

	public int getIndex() {
		return index;
	}

	public boolean isAutoincrementEnabled() {
		return autoincrementEnabled;
	}

	public void setAutoincrementEnabled(boolean autoincrementEnabled) {
		this.autoincrementEnabled = autoincrementEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void primitive(boolean value) {
		Array.setBoolean(primitiveArray, index, value);
		incrementIfEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void primitive(byte value) {
		Array.setByte(primitiveArray, index, value);
		incrementIfEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void primitive(char value) {
		Array.setChar(primitiveArray, index, value);
		incrementIfEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void primitive(short value) {
		Array.setShort(primitiveArray, index, value);
		incrementIfEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void primitive(int value) {
		Array.setInt(primitiveArray, index, value);
		incrementIfEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void primitive(long value) {
		Array.setLong(primitiveArray, index, value);
		incrementIfEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void primitive(float value) {
		Array.setFloat(primitiveArray, index, value);
		incrementIfEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void primitive(double value) {
		Array.setDouble(primitiveArray, index, value);
		incrementIfEnabled();
	}

}
