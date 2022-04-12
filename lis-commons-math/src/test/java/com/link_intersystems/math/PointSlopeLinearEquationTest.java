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

import static junit.framework.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class PointSlopeLinearEquationTest extends AbstractLinearEquationTest {

	@Override
	protected LinearEquation createBlueLinearEquation() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(-1.0, 0,
				2.5);
		return linearEquation;
	}

	@Override
	protected LinearEquation createRedLinearEquation() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(0.5, -2, 0);
		return linearEquation;
	}

	@Test
	public void withB() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(0.5, 1);
		assertReadLinearEquation(linearEquation);
	}

	@Test
	public void mOnly() {
		LinearEquation linearEquation = new PointSlopeLinearEquation(0.5);
		double y = linearEquation.fX(0);
		assertEquals(0.0, y);

		y = linearEquation.fX(2);
		assertEquals(1.0, y);

		y = linearEquation.fX(-2);
		assertEquals(-1.0, y);
	}

}
