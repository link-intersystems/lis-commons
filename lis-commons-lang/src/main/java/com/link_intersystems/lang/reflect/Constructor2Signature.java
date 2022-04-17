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

/**
 * Implements the signature equality logic for a {@link Constructor2}. A
 * constructor's signature is considered equal to another constructor's
 * signature if both's
 * <ul>
 * <li>declaring class is equal</li>
 * <li>parameter types are equal</li>
 * </ul>
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @param <T>
 *            the type that this constructor belongs to.
 * @since 1.2.0;
 */
class Constructor2Signature<T> extends Member2Signature<Constructor2<?>> {

	Constructor2Signature(Constructor2<?> constructor) {
		super(constructor);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Constructor2Signature<T> other = Constructor2Signature.class.cast(obj);

		Constructor2<?> otherConstructor = other.getMember2();
		Constructor2<?> constructor2 = getMember2();
		return constructor2.isDeclaringClassEqual(otherConstructor)
				&& constructor2.areParametersEqual(otherConstructor);
	}

	@Override
	public int hashCode() {
		Member2<?> member = getMember2();
		return member.hashCode();
	}

}