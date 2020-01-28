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
package com.link_intersystems.lang;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the {@link Conversions} identity conversion as defined by the java
 * language specification 5.1.1 Identity Conversions.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 */
public class ConversionsIdentityTest {

	/**
	 * A conversion from a type to that same type is permitted for any type.
	 */
	@Test
	public void nonPrimitiveTo() {
		assertTrue(Conversions.isIdentity(Byte.TYPE, Byte.TYPE));
		assertFalse(Conversions.isIdentity(Byte.TYPE, Double.TYPE));
	}

}
