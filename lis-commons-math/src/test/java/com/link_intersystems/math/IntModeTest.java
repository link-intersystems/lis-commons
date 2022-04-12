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

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class IntModeTest {

	@Test
	public void modeNoValues() {
		Mode<Integer> mode = new IntMode();
		Integer value = mode.getValue();
		Assert.assertNull(value);
	}

	@Test
	public void mode() {
		Mode<Integer> mode = new IntMode();
		mode.addValue(1);
		mode.addValue(2);
		mode.addValue(3);
		mode.addValue(4);
		mode.addValue(2);
		mode.addValue(2.2);
		mode.addValue(2);
		mode.addValue(1);
		mode.addValue(1);
		Integer value = mode.getValue();
		Assert.assertEquals(2, value.intValue());
	}

	@Test
	public void modes() {
		Mode<Integer> mode = new IntMode();
		mode.addValue(1);
		mode.addValue(1);
		mode.addValue(1);
		mode.addValue(2);
		mode.addValue(2);
		mode.addValue(2);
		mode.addValue(3);
		mode.addValue(3);
		mode.addValue(3);
		mode.addValue(4);
		mode.addValue(4);
		mode.addValue(5);
		List<Integer> modes = mode.getValues();
		Assert.assertTrue(modes.contains(1));
		Assert.assertTrue(modes.contains(2));
		Assert.assertTrue(modes.contains(3));
	}

	@Test
	public void modeChanged() {
		Mode<Integer> mode = new IntMode();
		boolean changed = mode.addValue(1);
		Assert.assertTrue(changed);
		changed = mode.addValue(1);
		Assert.assertFalse(changed);
		changed = mode.addValue(2);
		Assert.assertFalse(changed);
		changed = mode.addValue(2);
		Assert.assertTrue(changed);
		changed = mode.addValue(1);
		Assert.assertTrue(changed);
		changed = mode.addValue(3);
		Assert.assertFalse(changed);
		changed = mode.addValue(3);
		Assert.assertFalse(changed);
		changed = mode.addValue(3);
		Assert.assertTrue(changed);
	}
}
