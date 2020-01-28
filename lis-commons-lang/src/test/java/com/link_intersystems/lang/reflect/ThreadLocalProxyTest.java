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

import org.junit.Assert;
import org.junit.Test;

import com.link_intersystems.lang.ref.HardReference;
import com.link_intersystems.lang.ref.Reference;

public class ThreadLocalProxyTest {

	private String string1 = "string1";
	private String string2 = "string2";
	private static final ThreadLocal<Reference<String>> THREAD_LOCAL = new ThreadLocal<Reference<String>>();

	@Test
	public void proxyTest() {
		Reference<String> referenceProxy = ThreadLocalProxy.createProxy(
				THREAD_LOCAL, new HardReference<String>(null), Reference.class);

		String string = referenceProxy.get();
		Assert.assertNull(string);

		THREAD_LOCAL.set(new HardReference<String>(string1));
		string = referenceProxy.get();
		Assert.assertEquals(string1, string);

		THREAD_LOCAL.set(new HardReference<String>(string2));
		string = referenceProxy.get();
		Assert.assertEquals(string2, string);

		THREAD_LOCAL.remove();
		string = referenceProxy.get();
		Assert.assertNull(string);
	}
}
