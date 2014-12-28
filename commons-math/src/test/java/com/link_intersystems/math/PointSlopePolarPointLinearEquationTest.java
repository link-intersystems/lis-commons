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

public class PointSlopePolarPointLinearEquationTest extends
		AbstractLinearEquationTest {

	@Override
	protected LinearEquation createBlueLinearEquation() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(-1.0,
				new PolarPoint(2.5, 90.0));
		return linearEquation;
	}

	@Override
	protected LinearEquation createRedLinearEquation() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(0.5,
				new PolarPoint(2.0, 180.0));
		return linearEquation;
	}

	@Test
	public void doubleSlopeOnly() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(1.5);
		double fX = linearEquation.fX(1);
		Assert.assertEquals(1.5, fX, 0.001);

		double fY = linearEquation.fY(1.5);
		Assert.assertEquals(1.0, fY, 0.001);
	}

	@Test
	public void slopeOnly() {
		Slope slope = new Slope(1.5);
		LinearEquation linearEquation = new PointSlopeLinearEquation(slope);
		double fX = linearEquation.fX(1);
		Assert.assertEquals(1.5, fX, 0.001);

		double fY = linearEquation.fY(1.5);
		Assert.assertEquals(1.0, fY, 0.001);
	}

	@Test
	public void slopeWithB() {
		Slope slope = new Slope(1.5);
		LinearEquation linearEquation = new PointSlopeLinearEquation(slope, 1);
		double fX = linearEquation.fX(1);
		Assert.assertEquals(2.5, fX, 0.001);

		double fY = linearEquation.fY(2.5);
		Assert.assertEquals(1.0, fY, 0.001);
	}

	@Test
	public void slopeWithXandY() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(1.5, 1,
				1.5);
		double fX = linearEquation.fX(1);
		Assert.assertEquals(1.5, fX, 0.001);

		double fY = linearEquation.fY(1.5);
		Assert.assertEquals(1.0, fY, 0.001);
	}

	@Test
	public void dobleSlopeWithB() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(1.5, 1.0);
		double fX = linearEquation.fX(1);
		Assert.assertEquals(2.5, fX, 0.001);

		double fY = linearEquation.fY(2.5);
		Assert.assertEquals(1.0, fY, 0.001);
	}

	@Test
	public void doubleSlopeWithCartesianPoint() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(1.5,
				new CartesianPoint(0, 1.0));
		double fX = linearEquation.fX(1);
		Assert.assertEquals(2.5, fX, 0.001);

		double fY = linearEquation.fY(2.5);
		Assert.assertEquals(1.0, fY, 0.001);
	}
}
