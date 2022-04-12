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
import org.junit.jupiter.api.Test;

public class SlopeTest {

	@Test
	public void slopeForCartesianPoint() {
		CartesianPoint p1 = new CartesianPoint(0, 2.5);
		CartesianPoint p2 = new CartesianPoint(2.5, 0);
		Slope slope = new Slope(p1, p2);
		double value = slope.getValue();
		Assert.assertEquals(-1.0, value, 0.001);
	}

	@Test
	public void slopeForCartesianPoints() {
		Slope slope = new Slope(0, 2.5, 2.5, 0);
		double value = slope.getValue();
		Assert.assertEquals(-1.0, value, 0.001);
	}

	@Test
	public void slopeForPolarPoints() {
		PolarPoint p1 = new PolarPoint(2.5, 90.0);
		PolarPoint p2 = new PolarPoint(2.5, 0);
		Slope slope = new Slope(p1, p2);
		double value = slope.getValue();
		Assert.assertEquals(-1.0, value, 0.001);
	}
}
