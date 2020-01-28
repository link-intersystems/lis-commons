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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.concurrent.Callable;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.ClassLoaderContextAware;

/**
 * Implements the adaption for {@link Invokable#asCallable(Object...)},
 * {@link Invokable#asRunnable(Object...)}, {@link Invokable#invoke(Object...)}
 * and {@link Invokable#invokeWithContextClassLoader(ClassLoader, Object...)}
 * and delegates to the subclass's {@link #doInvoke(Object...)} method.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @version 1.2.0.0
 */
public abstract class AbstractInvokable implements Invokable {

	/**
	 * As the javadoc of {@link Invokable} states: {@link Invokable}s are
	 * accessible by default.
	 */
	private boolean accessible = true;

	public AbstractInvokable() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @version 1.2.0.0
	 */
	@SuppressWarnings("unchecked")
	public <T> T invoke(Object... args) throws Exception {
		Assert.notNull("args", args);
		try {
			if (!isApplicable(args)) {
				Class<?>[] argClasses = argsToClasses(args);
				throw new IllegalArgumentException(this
						+ " is not applicable for the argument types "
						+ Arrays.toString(argClasses));
			}
			ensureAccessibilityIsHonored();
			Object result = doInvoke(args);
			return (T) result;
		} catch (RuntimeException e) {
			throw e;
		} catch (InvocationTargetException e) {
			Throwable targetThrowable = e.getTargetException();
			if (targetThrowable instanceof Exception) {
				Exception targetException = Exception.class
						.cast(targetThrowable);
				boolean declaredException = isDeclaredException(targetException);
				if (not(declaredException)
						&& not(isRuntimeException(targetException))) {
					throw new UndeclaredThrowableException(targetThrowable);
				} else {
					throw targetException;
				}
			} else {
				throw new UndeclaredThrowableException(e);
			}
		} catch (Exception e) {
			boolean declaredException = isDeclaredException(e);
			if (not(declaredException)) {
				throw new UndeclaredThrowableException(e);
			} else {
				throw e;
			}
		}
	}

	private void ensureAccessibilityIsHonored() {
		AccessibleObject accessibleObject = getAccessibleObject();
		if (accessibleObject != null) {
			accessibleObject.setAccessible(accessible);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean isAccessible() {
		return accessible;
	}

	/**
	 * Subclasses should implement this method if they operate on
	 * {@link AccessibleObject}s.
	 * 
	 * @return
	 */
	protected AccessibleObject getAccessibleObject() {
		return null;
	}

	private Class<?>[] argsToClasses(Object[] args) {
		Class<?>[] argClasses = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			Class<?> argClass = null;
			if (arg != null) {
				argClass = arg.getClass();
			}
			argClasses[i] = argClass;
		}
		return argClasses;
	}

	/**
	 * Subclasses should implement this method to decide if this
	 * {@link Invokable} is applicable for the arguments that is currently
	 * invoked with. This method is called by the template method
	 * {@link #invoke(Object...)} and lets subclasses hook into the invocation
	 * process to support applicable invocations only. This default
	 * implementation always returns true.
	 * 
	 * @param params
	 *            the parameters that this {@link Invokable}'s
	 *            {@link Invokable#invoke(Object...)} method is invoked with.
	 * @return
	 * @since 1.2.0.0
	 */
	protected boolean isApplicable(Object... params) {
		return true;
	}

	private boolean isRuntimeException(Exception targetException) {
		return targetException instanceof RuntimeException;
	}

	/**
	 * Subclasses might override this method to decide if an occurred exception
	 * is declared for this {@link Invokable}.
	 * 
	 * @param e
	 * @return
	 * @since 1.2.0.0
	 */
	protected boolean isDeclaredException(Exception e) {
		return false;
	}

	/*
	 * Helper method to increase readability of conditional statements.
	 */
	private boolean not(boolean value) {
		return !value;
	}

	/**
	 * Called by the template method {@link #invoke(Object...)} after:
	 * <ul>
	 * <li>the {@link #invoke(Object...)}'s methods argument has been checked to
	 * be not null (an array or an empty arguments array has been passed to the
	 * invocation).</li>
	 * <li>the arguments are applicable for this {@link Invokable} (checked by
	 * calling {@link #isApplicable(Object...)} - subclasses should implement)</li>
	 * </ul>
	 * 
	 * Any thrown exception is handled by the template method
	 * {@link #invoke(Object...)}:
	 * <ul>
	 * <li>{@link RuntimeException}s are re-thrown.</li>
	 * <li>checked exceptions are re-thrown if they are declared by this
	 * {@link Invokable}.</li>
	 * <li>undeclared checked exceptions are wrapped into an
	 * {@link InvocationTargetException} and thrown.</li>
	 * </ul>
	 * 
	 * 
	 * @param args
	 *            the checked invocation arguments. See the method' javadoc for
	 *            details.
	 * @return the invocation result of this {@link Invokable}.
	 * @throws Exception
	 *             a declared or undeclared exception that occurred during
	 *             invocation of this {@link Invokable}. See javadoc for
	 *             exception handling details done by the template method
	 *             {@link #invoke(Object...)} that calls
	 *             {@link #doInvoke(Object...)}.
	 * @since 1.2.0.0
	 */
	protected abstract <T> T doInvoke(Object... args) throws Exception;

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2.0.0
	 */
	public <RESULT_TYPE> RESULT_TYPE invokeWithContextClassLoader(
			ClassLoader classLoader, Object... params) throws Exception {
		ClassLoaderContextAware classLoaderContext = ClassLoaderContextAware
				.forClassLoader(classLoader);
		Callable<RESULT_TYPE> callable = asCallable(params);

		try {
			RESULT_TYPE resut;
			resut = classLoaderContext.runInContext(callable);
			return resut;
		} catch (Exception e) {
			if (isDeclaredException(e)) {
				throw e;
			} else {
				throw new UndeclaredThrowableException(e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2.0.0
	 */
	public final Runnable asRunnable(final Object... params) {
		return new Runnable() {

			public void run() {
				try {
					invoke(params);
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2.0.0
	 */
	public final <T> Callable<T> asCallable(final Object... params) {
		return new Callable<T>() {

			@SuppressWarnings("unchecked")
			public T call() throws Exception {
				Object invokationResult = invoke(params);
				return (T) invokationResult;
			}

		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2.0.0
	 */
	public <T> PrivilegedExceptionAction<T> asPrivilegedExceptionAction(
			final Object... params) {
		return new PrivilegedExceptionAction<T>() {

			@SuppressWarnings("unchecked")
			public T run() throws Exception {
				Object invokationResult = invoke(params);
				return (T) invokationResult;
			}
		};
	}
}