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

import java.lang.reflect.Method;

import com.link_intersystems.lang.Primitives;

/**
 * An {@link NullInvocationArgumentsResolver} returns argument arrays that
 * contain null values for objects and default values for primitive types as
 * defined by {@link Primitives#getDefaultValue(Class)}.
 *
 * @author René Link [<a
 *         href="mailto:rene.link@link-intersystems.com">rene.link@link-
 *         intersystems.com</a>]
 * @since 1.2.0.0
 *
 */
public final class NullInvocationArgumentsResolver implements
		InvocationArgumentsResolver {

	/**
	 * Singleton instance of a {@link NullInvocationArgumentsResolver}.
	 */
	public static final InvocationArgumentsResolver INSTANCE = new NullInvocationArgumentsResolver();

	NullInvocationArgumentsResolver() {
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.2.0.0
	 */
	public Object[] getArguments(Object invokedObject, Method invokedMethod) {
		Class<?>[] argumentTypes = invokedMethod.getParameterTypes();
		Object[] arguments = new Object[argumentTypes.length];
		for (int i = 0; i < argumentTypes.length; i++) {
			Class<?> argumentType = argumentTypes[i];
			if (Primitives.isPrimitive(argumentType)) {
				Object defaultValue = Primitives.getDefaultValue(argumentType);
				arguments[i] = defaultValue;
			}
		}
		return arguments;
	}
}
