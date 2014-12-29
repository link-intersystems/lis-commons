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

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import com.link_intersystems.lang.Signature;

/**
 * A {@link Constructor2} is an extension of the standard java
 * {@link Constructor}. It adapts the standard java {@link Constructor} to the
 * abstract {@link Invokable} api and adds additional methods useful when
 * resolving an applicable constructor at runtime.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 * 
 */
public class Constructor2<T> extends Member2<Constructor<T>> {

	/**
	 *
	 */
	private static final long serialVersionUID = 9148588964585802716L;

	private Invokable invokable;

	private transient Signature signature;

	/**
	 * Constructs a {@link Constructor2} for the constructor.
	 * 
	 * @param constructor
	 *            the constructor that is the base for this {@link Constructor2}
	 *            .
	 * @since 1.0.0.0
	 */
	Constructor2(Constructor<T> constructor) {
		super(constructor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0.0
	 */
	public Type[] getGenericParameterTypes() {
		return getMember().getGenericParameterTypes();
	}

	public Invokable getInvokable() {
		if (invokable == null) {
			invokable = new Constructor2Invokable(this);
		}
		return invokable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0.0
	 */
	public Class<?>[] getParameterTypes() {
		return getMember().getParameterTypes();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0.0
	 */
	protected Class<?> getReturnType() {
		return getMember().getDeclaringClass();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2.0.0
	 */
	@Override
	public Class<?>[] getDeclaredExceptionTypes() {
		return getMember().getExceptionTypes();
	}

	/**
	 * @return a {@link Signature} that represents this constructor. Clients
	 *         might use the {@link Signature} to compare it to others. The
	 *         returned {@link Signature}'s attributes are
	 *         <ul>
	 *         <li>name</li>
	 *         <li>parameter types</li>
	 *         </ul>
	 *         Two {@link Signature}s are equal if these attributes are equal.
	 *         Therefore two constructor signatures can be equal even if they do
	 *         not belong to the same declaring class (e.g. same class name in
	 *         different packages).
	 * @since 1.0.0.0
	 */
	public Signature getSignature() {
		if (signature == null) {
			signature = new Constructor2Signature<T>(this);
		}
		return signature;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0.0
	 */
	public boolean isVarArgs() {
		return getMember().isVarArgs();
	}

	/**
	 * Constructs a new instance of the type T that this constructor constructs.
	 * An applicable constructor will be used for the given constructorArgs.
	 * 
	 * @param constructorArgs
	 * @throws IllegalArgumentException
	 *             if this constructor is not applicable for the constructor
	 *             arguments.
	 * @return a new instance of the type that this constructor constructs.
	 * @throws Exception
	 *             if one of the declared exceptions (if any) of the constructor
	 *             is thrown.
	 * @since 1.2.0.0
	 */
	@SuppressWarnings("unchecked")
	public T newInstance(Object... constructorArgs) throws Exception {
		Invokable invokable = getInvokable();
		Object newInstance = invokable.invoke(constructorArgs);
		return (T) newInstance;
	}
}
