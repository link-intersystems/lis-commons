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
 * A signature is a collection of attributes that identify an object. The
 * {@link Signature} interface does not specify any specific methods, because
 * the normal use case is to find out if two {@link Signature}s are equal and
 * get a string representation and these methods are already defined by
 * {@link Object}.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public interface Signature {

	/**
	 * @param obj
	 * @return true if this {@link Signature} and the other {@link Signature}
	 *         are equal according to the collection of attributes they have.
	 * @since 1.0.0.0
	 */
	@Override
	public boolean equals(Object obj);

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	@Override
	public int hashCode();

	/**
	 * @return a string representation of this signature. It is recommended, but
	 *         not required, for two {@link Signature}s to return the same
	 *         string representation if they are equal.
	 * @since 1.0.0.0
	 */
	@Override
	public String toString();

}
