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
import java.util.Arrays;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.Conversions;

/**
 * A description of an {@link Invokable}s {@link Parameter}.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0;
 * @see Method2
 * @see Constructor2
 */
public class Parameter implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8652346202323983694L;

	private final int index;
	private Member2<?> member2;

	Parameter(Member2<?> member2, int index) {
		Assert.notNull("member2", member2);
		if (index < 0 || index >= member2.getParameterTypes().length) {
			throw new IndexOutOfBoundsException(
					"index is out of parameter bounds. Parameter types are "
							+ Arrays.asList(member2.getParameterTypes()));
		}
		this.member2 = member2;
		this.index = index;
	}

	/**
	 * Returns the type of the Assert.
	 *
	 * @return the type of the this {@link Parameter}.
	 * @since 1.0.0;
	 */
	public Class2<?> getParameterClass2() {
		Class<?> type = getParameterClass();
		Class2<?> class2 = Class2.get(type);
		return class2;
	}

	private Class<?> getParameterClass() {
		Class<?> type = member2.getParameterTypes()[index];
		return type;
	}

	/**
	 * Returns true if this {@link Parameter} represents a variable argument
	 * {@link Parameter}.
	 *
	 * @return true if this {@link Parameter} represents a variable argument
	 *         {@link Parameter}.
	 * @since 1.0.0;
	 */
	public boolean isVarArg() {
		/*
		 * is this parameter the last one of a varargs method?
		 */
		return member2.isVarArgs()
				&& (member2.getParameterTypes().length - 1) == index;
	}

	/**
	 * Returns true if the type is an applicable subtype of the type that this
	 * {@link Parameter} represents.
	 *
	 * @param type
	 * @return true if the type is an applicable subtype of the type that this
	 *         {@link Parameter} represents.
	 * @since 1.0.0;
	 */
	boolean isApplicableSubtype(Class<?> type) {
		if (type == null) {
			/*
			 * null reference can be any type
			 */
			return true;
		}
		Class<?> supertype = getParameterClass2().getType();
		return supertype.isAssignableFrom(type);
	}

	private Class<?> getComparableType() {
		Class<?> supertype = getParameterClass2().getType();
		if (isVarArg()) {
			supertype = getParameterClass2().getType().getComponentType();
		}
		return supertype;
	}

	/**
	 * The name is either looked up via bytecode inspection or a default value
	 * (arg0, arg1, etc.) is returned if the bytecode does not contain debug
	 * information.
	 *
	 * @return the name of this parameter.
	 */
	public String getName() {
		return String.format("arg%s", index);
	}

	/**
	 * 15.12.2.3 Phase 2: Identify Matching Arity Methods Applicable by Method
	 * Invocation Conversion.
	 *
	 * @param invocationParameter
	 * @return
	 * @since 1.0.0;
	 */
	boolean isApplicableMethodInvocationConversion(Class<?> invocationParameter) {
		Class<?> declarationParameter = getParameterClass2().getType();
		boolean applicableMethodInvocationConversion = isApplicableMethodInvocationConversion(
				declarationParameter, invocationParameter);
		return applicableMethodInvocationConversion;
	}

	/**
	 *
	 * @param invocationParameter
	 * @return
	 * @since 1.0.0;
	 */
	boolean isApplicableVariableArityMethodInvocationConversion(
			Class<?> invocationParameter) {
		Class<?> comparableType = getComparableType();
		boolean applicableMethodInvocationConversion = isApplicableMethodInvocationConversion(
				comparableType, invocationParameter);
		return applicableMethodInvocationConversion;
	}

	private boolean isApplicableMethodInvocationConversion(
			Class<?> compareType, Class<?> invocationParameter) {
		/*
		 * Method invocation conversion is applied to each argument value in a
		 * method or constructor invocation (§8.8.7.1, §15.9, §15.12): the type
		 * of the argument expression must be converted to the type of the
		 * corresponding Assert. Method invocation contexts allow the use of one
		 * of the following:
		 */
		/*
		 * an identity conversion (§5.1.1)
		 */
		boolean identity = Conversions.isIdentity(invocationParameter,
				compareType);
		if (identity) {
			return true;
		}

		/*
		 * a widening primitive conversion (§5.1.2)
		 */
		if (invocationParameter.isPrimitive() && compareType.isPrimitive()) {
			boolean primitiveWidening = Conversions.isPrimitiveWidening(
					invocationParameter, compareType);
			if (primitiveWidening) {
				return true;
			}
		}
		/*
		 * a widening reference conversion (§5.1.5)
		 */
		boolean wideningReference = Conversions.isWideningReference(
				invocationParameter, compareType);
		if (wideningReference) {
			return true;
		}
		/*
		 * a boxing conversion (§5.1.7) optionally followed by widening
		 * reference conversion
		 */
		boolean boxingConversion = Conversions.isBoxingConversion(
				invocationParameter, compareType);
		if (boxingConversion) {
			return true;
		}
		/*
		 * an unboxing conversion (§5.1.8) optionally followed by a widening
		 * primitive conversion.
		 */
		boolean unboxingConversion = Conversions.isUnboxingConversion(
				invocationParameter, compareType);
		if (unboxingConversion) {
			return true;
		}
		return false;
	}

	public String toString() {
		String string = getParameterClass2().toString() + " arg" + index;
		return string;
	}

	boolean isApplicableAutobixing(Class<?> invocationParameter) {
		boolean boxingConversion = Conversions.isBoxingConversion(
				getParameterClass(), invocationParameter);
		return boxingConversion;
	}

}
