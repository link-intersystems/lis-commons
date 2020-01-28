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

/**
 * {@link Invokable} adaption for a {@link Constructor2}.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0.0
 * @see Constructor2#getInvokable()
 */
class Constructor2Invokable extends AbstractMemberInvokable<Constructor2<?>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4593028092177102980L;

	Constructor2Invokable(Constructor2<?> constructor2) {
		super(null, constructor2);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	@SuppressWarnings("unchecked")
	protected <T> T doInvoke(Object... params) throws Exception {
		Constructor2<?> constructor2 = getInvokableMember();
		Constructor<?> constructor = constructor2.getMember();
		T newObjectInstance = (T) constructor.newInstance(params);
		return newObjectInstance;
	}

}