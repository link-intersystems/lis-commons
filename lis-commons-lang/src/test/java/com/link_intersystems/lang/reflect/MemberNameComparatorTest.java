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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Test;

public class MemberNameComparatorTest {

	@Test
	public void compareNullValues() {
		int compare = ReflectFacade.getMemberNameComparator().compare(null,
				null);
		assertEquals(0, compare);
	}

	@Test
	public void compareNullWithMember() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		int compare = ReflectFacade.getMemberNameComparator().compare(null,
				declaredMethod);
		assertEquals(-1, compare);
	}

	@Test
	public void compareMemberWithNull() throws SecurityException,
			NoSuchMethodException {
		Method declaredMethod = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		int compare = ReflectFacade.getMemberNameComparator().compare(
				declaredMethod, null);
		assertEquals(1, compare);
	}

	@Test
	public void compareMembers() throws SecurityException,
			NoSuchMethodException {
		Method addMethod = ArrayList.class.getDeclaredMethod("add",
				Object.class);
		Method sizeMethod = ArrayList.class.getDeclaredMethod("size");
		int compare = ReflectFacade.getMemberNameComparator().compare(
				addMethod, sizeMethod);
		assertTrue(compare < 0);
		compare = ReflectFacade.getMemberNameComparator().compare(sizeMethod,
				addMethod);
		assertTrue(compare > 0);
	}
}
