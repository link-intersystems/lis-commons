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

/**
 * A callback for primitive types. Mostly used with
 * {@link Conversions#unbox(Object, PrimitiveCallback)}.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @since 1.0.0;
 * 
 */
public interface PrimitiveCallback {

	/**
	 * Callback method for boolean value.
	 * 
	 * @param value
	 * 
	 * @since 1.0.0;
	 */
	void primitive(boolean value);

	/**
	 * Callback method for byte value.
	 * 
	 * @param value
	 * 
	 * @since 1.0.0;
	 */
	void primitive(byte value);

	/**
	 * Callback method for char value.
	 * 
	 * @param value
	 * 
	 * @since 1.0.0;
	 */
	void primitive(char value);

	/**
	 * Callback method for short value.
	 * 
	 * @param value
	 * 
	 * @since 1.0.0;
	 */
	void primitive(short value);

	/**
	 * Callback method for int value.
	 * 
	 * @param value
	 * 
	 * @since 1.0.0;
	 */
	void primitive(int value);

	/**
	 * Callback method for long value.
	 * 
	 * @param value
	 * 
	 * @since 1.0.0;
	 */
	void primitive(long value);

	/**
	 * Callback method for float value.
	 * 
	 * @param value
	 * 
	 * @since 1.0.0;
	 */
	void primitive(float value);

	/**
	 * Callback method for double value.
	 * 
	 * @param value
	 * 
	 * @since 1.0.0;
	 */
	void primitive(double value);
}
