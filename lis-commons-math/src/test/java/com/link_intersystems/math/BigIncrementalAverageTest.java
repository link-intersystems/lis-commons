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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

public class BigIncrementalAverageTest extends AbstractAverageTest {

	@SuppressWarnings("unchecked")
	@Override
	protected <N extends Number> Average<N> createAverage() {
		return (Average<N>) new BigIncrementalAverage();
	}

	@Test
	public void bigDecimalValues() {
		BigDecimal value1 = new BigDecimal("1436896656.3447867874526");
		BigDecimal value2 = new BigDecimal("78974343443.56795632589876543");
		BigDecimal value3 = new BigDecimal("5543765892147894.434365743");

		BigDecimal expected = new BigDecimal(
				"1847948767795998.11570295211712181");

		Average<BigDecimal> average = createAverage();
		average.addValue(value1);
		average.addValue(value2);
		average.addValue(value3);
		BigDecimal averageValue = average.getValue();
		assertEquals(expected, averageValue);
	}

	@Test
	public void bigIntegerValues() {
		BigInteger value1 = new BigInteger("14368966563447867874526");
		BigInteger value2 = new BigInteger("7897434344356795632589876543");
		BigInteger value3 = new BigInteger("5543765892147894434365743");

		BigDecimal expected = new BigDecimal("2634330826405168991630705604");

		Average<BigDecimal> average = createAverage();
		average.addValue(value1);
		average.addValue(value2);
		average.addValue(value3);
		BigDecimal averageValue = average.getValue();
		assertEquals(expected, averageValue);
	}

	@Test
	public void valueChanged() {
		BigInteger value1 = new BigInteger("1");
		BigInteger value2 = new BigInteger("5");
		BigInteger value3 = new BigInteger("3");

		Average<BigDecimal> average = createAverage();
		boolean changed = average.addValue(value1);
		Assert.assertTrue(changed);
		changed = average.addValue(value2);
		Assert.assertTrue(changed);
		changed = average.addValue(value3);
		Assert.assertFalse(changed);
	}
}
