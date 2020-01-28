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
package com.link_intersystems.lang.ref;

import com.link_intersystems.lang.Assert;

/**
 * Adapts a {@link ThreadLocal} to a {@link Reference}.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @param <T>
 *            the type of the referenced object.
 * @since 1.2.0.0
 */
public class ThreadLocalReference<T> implements Reference<T> {

	private ThreadLocal<T> threadLocal;

	/**
	 * Constructs a {@link Reference} based on the {@link ThreadLocal}.
	 * 
	 * @param threadLocal
	 *            the {@link ThreadLocal} to call for the referent object.
	 * @since 1.2.0.0
	 */
	public ThreadLocalReference(ThreadLocal<T> threadLocal) {
		Assert.notNull("threadLocal", threadLocal);
		this.threadLocal = threadLocal;
	}

	/**
	 * @inherited
	 * @since 1.2.0.0
	 */
	public T get() {
		return threadLocal.get();
	}

}
