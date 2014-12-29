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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.Callable;

/**
 * An {@link Invokable} is an abstraction to all java members, e.g. a
 * {@link Method} or a {@link Constructor}, that can be invoked. All
 * {@link Invokable}s are accessible by default in terms of the
 * {@link AccessibleObject#setAccessible(boolean)} method.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public interface Invokable {

	/**
	 * Invokes the {@link Member2} that this {@link Invokable} has been created
	 * from on the target object that was passed to the factory method for this
	 * {@link Invokable}.
	 * 
	 * @param params
	 *            the parameter object to use when invoking.
	 * @return
	 * @throws Exception
	 *             if one of the declared exceptions (if any) of the invokable
	 *             is thrown.
	 * @see Method2#getInvokable(Object)
	 * @see Constructor2#getInvokable()
	 * 
	 * @since 1.0.0.0
	 * @version 1.2.0.0
	 */
	public abstract <T> T invoke(Object... params) throws Exception;

	/**
	 * Returns a {@link Runnable} adapter for this {@link Invokable}. If you are
	 * interested in the return type use {@link #asCallable(Object...)};
	 * 
	 * @param params
	 * @return a {@link Runnable} adapter for this {@link Invokable}. If you are
	 *         interested in the return type use {@link #asCallable(Object...)};
	 * @since 1.2.0.0
	 */
	public abstract Runnable asRunnable(Object... params);

	/**
	 * Returns a {@link Callable} adapter for this {@link Invokable} to
	 * integrate with the java.util.concurrent package.
	 * 
	 * @param args
	 *            the parameters that should be used when the {@link Callable}
	 *            is called.
	 * @return a {@link Callable} adapter for this {@link Invokable} to
	 *         integrate with the java.util.concurrent package.
	 * @since 1.2.0.0
	 */
	public abstract <T> Callable<T> asCallable(Object... args);

	/**
	 * Ensures that this {@link Invokable} is invoked with the given
	 * {@link ClassLoader} bound as the current {@link Thread}'s context class
	 * loader. Ensures that any previous context class loader is re-bound to the
	 * current {@link Thread}'s context when returning.
	 * 
	 * @param classLoader
	 *            the class loader that should be bound as the current thread's
	 *            context class loader during invocation of this
	 *            {@link Invokable}.
	 * 
	 * @param args
	 *            the parameter to pass to this {@link Invokable}.
	 * @return the result of the invocation of this {@link Invokable}.
	 * @throws Exception
	 * @since 1.2.0.0
	 */
	<RESULT_TYPE> RESULT_TYPE invokeWithContextClassLoader(
			ClassLoader classLoader, Object... args) throws Exception;

	/**
	 * Returns a {@link PrivilegedExceptionAction} adapter for this
	 * {@link Invokable}.
	 * 
	 * @param args
	 * @return a {@link PrivilegedExceptionAction} adapter for this
	 *         {@link Invokable}.
	 */
	<RESULT_TYPE> PrivilegedExceptionAction<RESULT_TYPE> asPrivilegedExceptionAction(
			Object... args);

	/**
	 * Set the accessibility property of this {@link Invokable} in terms of the
	 * {@link AccessibleObject#setAccessible(boolean)}.
	 * 
	 * @param accessible
	 *            if this invokable should be accessible or not in terms of
	 *            {@link AccessibleObject#setAccessible(boolean)}. Default is
	 *            true.
	 * @since 1.2.0.0
	 */
	public void setAccessible(boolean accessible);

	/**
	 * @return true if this {@link Invokable} is accessible in terms of the
	 *         {@link AccessibleObject#isAccessible()} method.
	 * 
	 * @since 1.2.0.0
	 */
	public boolean isAccessible();
}
