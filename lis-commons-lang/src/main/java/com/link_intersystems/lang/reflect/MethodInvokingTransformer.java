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
import java.lang.reflect.Modifier;
import java.util.function.Function;

import com.link_intersystems.lang.Assert;

/**
 * A {@link MethodInvokingTransformer} transforms objects by calling methods on
 * them and returns the method result as the transformed object.
 *
 * @author René Link [<a
 *         href="mailto:rene.link@link-intersystems.com">rene.link@link-
 *         intersystems.com</a>]
 * @version 1.0.0;
 */
public class MethodInvokingTransformer implements Function<Object, Object> {

	private Method2 methodToInvokeForTransformation;
	private final InvocationArgumentsResolver invocationArgumentsResolver;

	/**
	 *
	 * @param methodToUse
	 * @since 1.0.0;
	 */
	public MethodInvokingTransformer(Method methodToUse) {
		this(Method2.forMethod(methodToUse));
	}

	/**
	 *
	 * @param methodToUse
	 * @since 1.0.0;
	 */
	public MethodInvokingTransformer(Method2 methodToUse) {
		this(methodToUse, NullInvocationArgumentsResolver.INSTANCE);
	}

	/**
	 *
	 * @param methodToUse
	 * @param invocationArgumentsResolver
	 * @since 1.2.0;
	 */
	public MethodInvokingTransformer(Method2 methodToUse,
			InvocationArgumentsResolver invocationArgumentsResolver) {
		Assert.notNull("methodToUse", methodToUse);
		Assert.notNull("invocationArgumentsResolver", invocationArgumentsResolver);
		this.invocationArgumentsResolver = invocationArgumentsResolver;
		this.methodToInvokeForTransformation = methodToUse;
	}

	/**
	 *
	 * @param methodToUse
	 * @param invocationArgumentsResolver
	 * @since 1.2.0;
	 */
	public MethodInvokingTransformer(Method methodToUse,
			InvocationArgumentsResolver invocationArgumentsResolver) {
		this(Method2.forMethod(methodToUse), invocationArgumentsResolver);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The transformation logic is defined as:
	 * <ol>
	 * <li>if the input is null than null is returned</li>
	 * <li>if the input is not an instance of the type this
	 * {@link MethodInvokingTransformer} transforms (see constructor argument
	 * type) than the input is passed through.</li>
	 * <li>otherwise the input is transformed by calling the transform method as
	 * defined by the constructor with the arguments that are resolved by the
	 * {@link InvocationArgumentsResolver} passed to the constructor and
	 * returning the methods result as the transformed object.</li>
	 * </ol>
	 * </p>
	 *
	 * @throws IllegalStateException
	 *             if an exception occurs while invoking the transform method on
	 *             the input object.
	 * @since 1.0.0;
	 * @version 1.2.0;
	 */
	public Object apply(Object targetObject) {
		Object result = null;

		try {
			if (isStaticMethodInvokation(targetObject)) {
				result = invokeStaticMethod(targetObject);
			} else {
				result = invokeInstanceMethod(targetObject);
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to transform targetObject "
					+ targetObject + " by invoking "
					+ methodToInvokeForTransformation, e);
		}
		return result;
	}

	protected Object invokeStaticMethod(Object targetObject) throws Exception {
		if (targetObject == null) {
			targetObject = methodToInvokeForTransformation.getDeclaringClass();
		}
		Object[] arguments = invocationArgumentsResolver.getArguments(
				targetObject, methodToInvokeForTransformation.getMember());
		return invokeMethodWithArguments(targetObject, arguments);
	}

	protected Object invokeInstanceMethod(Object targetObject) throws Exception {
		if (targetObject == null) {
			throw new NullPointerException("Can not invoke instance method "
					+ methodToInvokeForTransformation + " on null target");
		}
		Object[] arguments = invocationArgumentsResolver.getArguments(
				targetObject, methodToInvokeForTransformation.getMember());
		return invokeMethodWithArguments(targetObject, arguments);
	}

	private boolean isStaticMethodInvokation(Object targetObject) {
		boolean isNullTarget = targetObject == null;
		boolean isStaticMethodToInvoke = Modifier
				.isStatic(methodToInvokeForTransformation.getModifiers());
		if (isNullTarget && isStaticMethodToInvoke) {
			return true;
		}

		boolean isTargetObjectAClass = targetObject instanceof Class<?>;
		if (isTargetObjectAClass) {
			Class<?> targetClass = (Class<?>) targetObject;
			Class<?> declaringClass = methodToInvokeForTransformation
					.getDeclaringClass();
			return targetClass.isAssignableFrom(declaringClass);
		}
		return false;
	}

	protected Object invokeMethodWithArguments(Object targetObject,
			Object[] arguments) throws Exception {
		Invokable invokable = methodToInvokeForTransformation
				.getInvokable(targetObject);
		Object invokationResult = invokable.invoke(arguments);
		return invokationResult;
	}
}
