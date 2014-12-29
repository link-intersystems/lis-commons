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

import java.util.Collection;
import java.util.List;

import org.junit.Test;

/**
 * Tests the {@link Conversions} widening reference conversion as defined by the
 * java language specification 5.1.5 Widening Reference Conversions
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 */
public class ConversionsReferenceWideningTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullFrom() {
		Class<?> s = null;
		Class<?> t = Collection.class;
		Conversions.isWideningReference(s, t);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTo() {
		Class<?> s = List.class;
		Class<?> t = null;
		Conversions.isWideningReference(s, t);
	}

	/**
	 * A widening reference conversion exists from any type S to any type T,
	 * provided S is a subtype (§4.10) of T.
	 */
	@Test
	public void subtypeWidening() {
		Class<?> s = List.class;
		Class<?> t = Collection.class;
		boolean wideningReference = false;
		wideningReference = Conversions.isWideningReference(s, t);
		assertTrue(wideningReference);
		wideningReference = Conversions.isWideningReference(t, s);
		assertFalse(wideningReference);
	}

	@Test
	public void arrayWidening() {
		Class<?> s = List[].class;
		Class<?> t = Collection[].class;
		boolean wideningReference = false;
		wideningReference = Conversions.isWideningReference(s, t);
		assertTrue(wideningReference);
		wideningReference = Conversions.isWideningReference(t, s);
		assertFalse(wideningReference);
	}

}
