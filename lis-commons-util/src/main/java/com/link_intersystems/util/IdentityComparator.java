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
/**
 *
 */
package com.link_intersystems.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * An {@link IdentityComparator} of type T that compares objects by their
 * identity.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 * @since 1.0.0;
 */
public class IdentityComparator<T> implements Comparator<T>, Serializable {

	private static final long serialVersionUID = -4923287345290357681L;

	/**
	 * {@inheritDoc}
	 *
	 * @return 0 if the compared objects of the same instance (o1 == o2). Less
	 *         or greater values are calculated by the
	 *         {@link System#identityHashCode(Object)}. If the identity hash
	 *         code of o1 is less then o2 it is considered to be less.
	 * @since 1.0.0;
	 */
	public int compare(T o1, T o2) {
		int identityHashCode1 = System.identityHashCode(o1);
		int identityHashCode2 = System.identityHashCode(o2);
		int diff = identityHashCode1 - identityHashCode2;

		int returnDiff = 0;

		if (diff > 0) {
			returnDiff = 1;
		} else if (diff < 0) {
			returnDiff = -1;
		}

		return returnDiff;
	}
}