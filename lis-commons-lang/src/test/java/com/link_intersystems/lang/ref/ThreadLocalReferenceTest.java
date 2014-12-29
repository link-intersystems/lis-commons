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

import org.junit.Assert;
import org.junit.Test;

public class ThreadLocalReferenceTest {

	private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<String>();

	@Test
	public void getReferent() {
		ThreadLocalReference<String> threadLocalReference = new ThreadLocalReference<String>(
				THREAD_LOCAL);
		String referent = threadLocalReference.get();
		Assert.assertNull(referent);
		THREAD_LOCAL.set("TEST");
		referent = threadLocalReference.get();
		Assert.assertEquals("TEST", referent);
	}

	@Test(expected = IllegalArgumentException.class)
	public void newWithNullThreadLocal() {
		new ThreadLocalReference<String>(null);
	}
}
