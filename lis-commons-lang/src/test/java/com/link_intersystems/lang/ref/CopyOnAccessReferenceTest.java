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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;

import java.io.Serializable;

import org.junit.Test;

public class CopyOnAccessReferenceTest {

	@Test
	public void copyOnAccess() {
		StringTestHolder stringTestHolder = new StringTestHolder();
		stringTestHolder.string = "Test";

		Reference<StringTestHolder> copyOnAccessReference = new CopyOnAccessReference<StringTestHolder>(
				stringTestHolder);
		StringTestHolder copy = copyOnAccessReference.get();
		assertEquals(stringTestHolder, copy);
		assertNotSame(stringTestHolder, copy);

		assertEquals("Test", copy.string);

		stringTestHolder.string = "Test2";
		StringTestHolder copy2 = copyOnAccessReference.get();
		assertEquals(stringTestHolder, copy2);
		assertNotSame(stringTestHolder, copy2);

		assertEquals("Test2", copy2.string);
	}

	@Test
	public void copyOnAccessForNullRef() {
		Reference<StringTestHolder> copyOnAccessReference = new CopyOnAccessReference<StringTestHolder>(
				null);
		StringTestHolder copy = copyOnAccessReference.get();
		assertNull(copy);
	}
}

class StringTestHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String string;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringTestHolder other = (StringTestHolder) obj;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		return true;
	}

}
