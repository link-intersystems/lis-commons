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

/**
 * A {@link Reference} implementation that just holds a reference to the
 * referent object. The common use case is the adaption of an arbitrary
 * reference to a {@link Reference} object.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @param <T>
 * @since 1.0.0.0
 */
public class HardReference<T> implements Reference<T> {

	private final T t;

	public HardReference(T t) {
		this.t = t;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0.0
	 */
	public T get() {
		return t;
	}

}
