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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Unit test for the {@link Average} interface. Subclasses must return the
 * {@link Average} implementation.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 */
public abstract class AbstractAverageTest {

	@Test(expected = IllegalArgumentException.class)
	public void addNullValue() {
		Average<Number> average = createAverage();
		average.addValue(null);
	}

	@Test
	public void average() {
		Integer value1 = 3;
		Double value2 = 5.6;
		Integer value3 = 4;
		Long value4 = (long) 8;
		Double value5 = 4.4;

		Average<Number> average = createAverage();
		assertNotNull("createAverage must return an implementation.", average);
		average.addValue(value1);
		average.addValue(value2);
		average.addValue(value3);
		average.addValue(value4);
		average.addValue(value5);

		Number averageValue = average.getValue();
		assertEquals(5d, averageValue.doubleValue(), 0.0000001);

		average = createAverage();
		assertNotNull("createAverage must return an implementation.", average);

		average.addValue(value5);
		average.addValue(value3);
		average.addValue(value2);
		average.addValue(value4);
		average.addValue(value1);

		averageValue = average.getValue();
		assertEquals(5d, averageValue.doubleValue(), 0.0000001);
	}

	@Test
	public void averageDoesNotChange() {
		Average<Number> average = createAverage();
		assertNotNull("createAverage must return an implementation.", average);
		average.addValue(10);
		average.addValue(10);
		average.addValue(10);
		average.addValue(10);
		Number averageValue = average.getValue();
		assertEquals(10d, averageValue.doubleValue(), 0.0000001);
	}

	/**
	 * Tests the average calculation compared to a mathematically simple
	 * approach that first adds all values and then divides them by their count.
	 */
	@Test
	public void averageAlgorithmComparison() {
		double[] doubles = new double[] { 543.232, 367.32, 2358.23, 32782.257,
				23.43567, 232.235666, 23.43567, 232.2356662343567, 232.235666 };
		double averageDouble = 0.0;
		for (int i = 0; i < doubles.length; i++) {
			averageDouble += doubles[i];
		}
		averageDouble /= doubles.length;

		Average<Double> average = createAverage();
		for (int i = 0; i < doubles.length; i++) {
			average.addValue(doubles[i]);
		}
		Number average2 = average.getValue();

		assertEquals(averageDouble, average2.doubleValue(), 0.00000000001);
	}

	protected abstract <N extends Number> Average<N> createAverage();
}
