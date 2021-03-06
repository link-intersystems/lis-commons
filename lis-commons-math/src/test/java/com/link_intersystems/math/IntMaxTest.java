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
package com.link_intersystems.math;

import org.junit.Assert;
import org.junit.Test;

public class IntMaxTest {

	@Test
	public void maxNoValues() {
		Max<Integer> max = new IntMax();
		Integer value = max.getValue();
		Assert.assertNull(value);
	}

	@Test
	public void max() {
		Max<Integer> max = new IntMax();
		boolean valueChanged = max.addValue(5);
		Assert.assertTrue(valueChanged);
		valueChanged = max.addValue(5);
		Assert.assertFalse(valueChanged);
		max.addValue(2);
		max.addValue(9);
		max.addValue(1);
		max.addValue(-32);
		max.addValue(53.2);
		max.addValue(53.6);
		Integer value = max.getValue();
		Assert.assertEquals(53, value.intValue());
	}
}
