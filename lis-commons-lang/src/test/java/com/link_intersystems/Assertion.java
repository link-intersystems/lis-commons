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
package com.link_intersystems;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.Callable;

public class Assertion {

	public static void assertCause(Class<? extends Throwable> expectedCause,
			Callable<?> callable) throws Throwable {
		assertNotNull(callable, "callable must not be null");
		try {
			callable.call();
			throw new AssertionError("Expected exception " + expectedCause
					+ " was not thrown");
		} catch (Exception e) {
			Throwable cause = e.getCause();
			assertNotNull(cause, "cause must not be null");
			if (!expectedCause.isInstance(cause)) {
				throw cause;
			}
		}

	}

}
