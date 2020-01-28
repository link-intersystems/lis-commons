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

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class BigDecimalSumTest {

	@Test
	public void sumNoValues() {
		Sum<BigDecimal> sum = new BigDecimalSum();
		BigDecimal value = sum.getValue();
		Assert.assertEquals(0, value.doubleValue(), 0.000001);
	}

	@Test
	public void sum() {
		Sum<BigDecimal> sum = new BigDecimalSum();
		boolean changed = sum.addValue(5);
		Assert.assertTrue(changed);
		changed = sum.addValue(5);
		Assert.assertTrue(changed);
		changed = sum.addValue(2.3);
		Assert.assertTrue(changed);
		sum.addValue(9.2);
		sum.addValue(1.04);
		BigDecimal value = sum.getValue();
		Assert.assertEquals(22.54, value.doubleValue(), 0.000001);
	}

	@Test
	public void add0() {
		Sum<BigDecimal> sum = new BigDecimalSum();
		Assert.assertFalse(sum.addValue(BigDecimal.ZERO));
	}
}
