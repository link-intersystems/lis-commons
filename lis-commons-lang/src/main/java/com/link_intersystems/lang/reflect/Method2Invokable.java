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
 * {@link Invokable} adaption for a {@link Method2}.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0;
 * @see Method2#getInvokable(Object)
 */
class Method2Invokable extends AbstractMemberInvokable<Method2> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2877354538906078921L;

	Method2Invokable(Object target, Method2 method2) {
		super(target, method2);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0;
	 */
	@SuppressWarnings("unchecked")
	protected <T> T doInvoke(Object... args) throws Exception {
		Method2 method2 = getInvokableMember();
		Method method = method2.getMember();
		args = method2.toMethodInvokeParams(args);
		Object targetToInvoke = getTarget();
		T result = (T) method.invoke(targetToInvoke, args);
		return result;
	}

}