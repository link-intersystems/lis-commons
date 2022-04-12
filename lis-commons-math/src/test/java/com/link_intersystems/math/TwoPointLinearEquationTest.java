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

public class TwoPointLinearEquationTest extends AbstractLinearEquationTest {

	@Override
	protected LinearEquation createBlueLinearEquation() {
		LinearEquation linearEquation = new TwoPointLinearEquation(0, 2.5, 2.5,
				0);
		return linearEquation;
	}

	@Override
	protected LinearEquation createRedLinearEquation() {
		LinearEquation linearEquation = new TwoPointLinearEquation(-2, 0, 0, 1);
		return linearEquation;
	}

	@Test
	public void onePoint() {
		LinearEquation linearEquation = new TwoPointLinearEquation(1, 1);
		double fX = linearEquation.fX(0.5);
		Assert.assertEquals(0.5, fX, 0.00001);
	}
}
