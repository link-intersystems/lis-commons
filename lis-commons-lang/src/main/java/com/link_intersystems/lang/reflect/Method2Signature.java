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

import java.lang.reflect.Member;

/**
 * Implements the signature equality logic for a {@link Method2}. A method's
 * signature is considered equal to another method's signature if both's
 * <ul>
 * <li>name is equal</li>
 * <li>parameter types are equal</li>
 * </ul>
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0.0
 */
class Method2Signature extends Member2Signature<Method2> {

	Method2Signature(Method2 method) {
		super(method);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		} else {
			Method2Signature other = (Method2Signature) obj;
			Method2 method2 = getMember2();
			Method2 otherMethod2 = other.getMember2();

			return method2.isNameEqual(otherMethod2)
					&& method2.areParametersEqual(otherMethod2);
		}
	}

	@Override
	public int hashCode() {
		Member member = getMember();
		return member.hashCode();
	}

}