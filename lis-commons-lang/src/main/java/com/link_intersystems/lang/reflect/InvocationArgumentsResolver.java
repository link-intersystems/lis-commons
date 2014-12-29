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

/**
 * An {@link InvocationArgumentsResolver} resolves arguments for a method call.
 * 
 * @author René Link [<a
 *         href="mailto:rene.link@link-intersystems.com">rene.link@link-
 *         intersystems.com</a>]
 * @since 1.2.0.0
 */
public interface InvocationArgumentsResolver {

	/**
	 * 
	 * @param invokedObject
	 *            the object that the method will get invoked on.
	 * @param invokedMethod
	 *            the method that will be invoked on the object.
	 * @return an array of argument objects for invoking the method on the
	 *         object. The length of the returned argument object array must
	 *         match the length of the invoked method's parameter types.
	 * @since 1.0.0.0
	 */
	public Object[] getArguments(Object invokedObject, Method invokedMethod);
}
