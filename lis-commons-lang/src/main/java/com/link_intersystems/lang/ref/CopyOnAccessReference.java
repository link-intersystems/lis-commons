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

import com.link_intersystems.lang.Serialization;

import java.io.Serializable;


/**
 * A {@link Reference} that copies the referent on every {@link #get()} call by
 * serialization/deserialization.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 * @since 1.2.0.0
 */
public class CopyOnAccessReference<T extends Serializable> implements
		Reference<T> {

	private final T referentToCopyOnAccess;

	/**
	 * Constructs a new {@link CopyOnAccessReference} that copies the referent
	 * on every access ({@link #get()} invocation).
	 *
	 * @param referentToCopyOnAccess
	 *            the template referent.
	 * @since 1.2.0.0
	 */
	public CopyOnAccessReference(T referentToCopyOnAccess) {
		this.referentToCopyOnAccess = referentToCopyOnAccess;
	}

	/**
	 * {@inheritDoc}. Always returns a copy of the referent.
	 *
	 * @since 1.2.0.0
	 */
	public T get() {
		if (referentToCopyOnAccess == null) {
			return null;
		}
		T clone = Serialization.clone(referentToCopyOnAccess);
		return clone;
	}
}
