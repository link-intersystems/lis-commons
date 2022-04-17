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
package com.link_intersystems.lang.ref;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * An adaption for any {@link Serializable} object to the {@link Reference}
 * interface.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 *            the type that is known to be {@link Serializable}. This
 *            {@link Reference}'s type might be some type that is not
 *            {@link Serializable}, but the concrete object that this reference
 *            was created with is {@link Serializable}. E.g. a T is defined as
 *            {@link Set} and object that this
 *            {@link SerializableObjectReference} was created with is a
 *            {@link HashSet}.
 * @since 1.0.0;
 */
public class SerializableObjectReference<T> implements SerializableReference<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3782148440666397226L;

	private final T t;

	/**
	 *
	 * @param serializable
	 *            the serializable to adapt to the {@link Reference} interface
	 * @param <SERIALIZABLE_TYPE>
	 *            a {@link Serializable} object that is of type T as defined by
	 *            the class's type definition.
	 * @throws ClassCastException
	 *             if the serializable object is not of the type that this
	 *             class's type T defines.
	 * @since 1.0.0;
	 */
	@SuppressWarnings("unchecked")
	public <SERIALIZABLE_TYPE extends Serializable> SerializableObjectReference(
			SERIALIZABLE_TYPE serializable) {
		this.t = (T) serializable;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0;
	 */
	public T get() {
		return t;
	}
}
