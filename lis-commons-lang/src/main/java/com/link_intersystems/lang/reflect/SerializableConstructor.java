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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.SerializationException;
import com.link_intersystems.lang.ref.AbstractSerializableReference;
import com.link_intersystems.lang.ref.Reference;

/**
 * Represents a {@link Constructor} object {@link Reference} that is
 * {@link Serializable}.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @since 1.0.0.0
 *
 */
class SerializableConstructor extends
		AbstractSerializableReference<Constructor<?>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -6636456646515992155L;

	private static class ConstructorSerializationInfo extends
			MemberSerialization<Constructor<?>> {

		/**
		 *
		 */
		private static final long serialVersionUID = -8447552263782991338L;
		private Class<?>[] parameterTypes;

		public ConstructorSerializationInfo(Constructor<?> constructor) {
			super(constructor);
			parameterTypes = constructor.getParameterTypes();
		}

		private Class<?>[] getParameterTypes() {
			return parameterTypes;
		}

	}

	/**
	 * Constructs a serializable wrapper for the given constructor.
	 *
	 * @param constructor
	 *            that should be wrapped in a serializable way.
	 *
	 * @since 1.0.0.0
	 */
	public SerializableConstructor(Constructor<?> constructor) {
		super(constructor);
		Assert.notNull("constructor", constructor);
	}

	@Override
	protected Serializable serialize(Constructor<?> nonSerializableObject) {
		ConstructorSerializationInfo constructorSerializationInfo = new ConstructorSerializationInfo(
				nonSerializableObject);
		return constructorSerializationInfo;
	}

	@Override
	protected Constructor<?> deserialize(Serializable restoreInfo) {
		ConstructorSerializationInfo constructorSerializationInfo = (ConstructorSerializationInfo) restoreInfo;
		Class<?> declaringClass = constructorSerializationInfo
				.getDeclaringClass();
		Class<?>[] parameterTypes = constructorSerializationInfo
				.getParameterTypes();
		String methodName = constructorSerializationInfo.getMemberName();
		try {
			Constructor<?> constructor = getConstructor(declaringClass,
					parameterTypes);
			return constructor;
		} catch (NoSuchMethodException e) {
			throw new SerializationException("Unable to restore method "
					+ methodName + " declared at " + declaringClass
					+ " with parameter types " + Arrays.asList(parameterTypes),
					e);
		}
	}

	protected Constructor<?> getConstructor(Class<?> declaringClass,
			Class<?>[] parameterTypes) throws NoSuchMethodException {
		Constructor<?> constructor = declaringClass
				.getDeclaredConstructor(parameterTypes);
		return constructor;
	}

}
