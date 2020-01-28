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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Iterator;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Transformer;

/**
 * Transforms a type variable to it's string representation.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0.0
 */
class TypeVariableToStringTransformer implements Transformer<TypeVariable<?>, String> {

	public static final Transformer<TypeVariable<?>, String> INSTANCE = new TypeVariableToStringTransformer();

	public String transform(TypeVariable<?> typeVariable) {
		String genericTypeName = typeVariable.getName();
		StringBuilder toStringBuilder = new StringBuilder(genericTypeName);
		Type[] boundsArr = typeVariable.getBounds();

		if (boundsArr.length != 1 || !Object.class.equals(boundsArr[0])) {
			toStringBuilder.append(" extends ");
			Iterator<Type> boundsIterator = IteratorUtils
					.arrayIterator(boundsArr);

			while (boundsIterator.hasNext()) {
				Type boundsType = boundsIterator.next();

				if (boundsType instanceof Class<?>) {
					Class<?> classObj = Class.class.cast(boundsType);
					String classToString = classToString(classObj);
					toStringBuilder.append(classToString);
				} else if (boundsType instanceof TypeVariable<?>) {
					TypeVariable<?> boundsTypeVariable = (TypeVariable<?>) boundsType;
					String typeVariableAsString = typeVariableToString(boundsTypeVariable);
					toStringBuilder.append(typeVariableAsString);
				} else if (boundsType instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) boundsType;
					String string = parameterizedTypeToString(parameterizedType);
					toStringBuilder.append(string);
				} else {
					toStringBuilder.append("???");
				}

				if (boundsIterator.hasNext()) {
					toStringBuilder.append(" & ");
				}
			}
		}

		return toStringBuilder.toString();
	}

	private String classToString(Class<?> clazz) {
		Class2<?> clazz2 = Class2.get(clazz);
		return clazz2.toString();
	}

	private String typeVariableToString(TypeVariable<?> typeVariable) {
		String name = typeVariable.getName();
		return name;
	}

	private String parameterizedTypeToString(ParameterizedType parameterizedType) {
		return parameterizedType.toString();
	}
}