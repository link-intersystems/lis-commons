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
package com.link_intersystems.lang.reflect.criteria;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SelectionEnumTest {

	private final Result enumObj;

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] { { Result.FIRST },
				{ Result.ALL }, { Result.LAST }

		});
	}

	public SelectionEnumTest(Result enumObj) {
		this.enumObj = enumObj;
	}

	@Test
	public void valueOfAndName() {
		String name = enumObj.name();
		Result valueOf = Result.valueOf(name);
		assertEquals(enumObj, valueOf);
	}
}
