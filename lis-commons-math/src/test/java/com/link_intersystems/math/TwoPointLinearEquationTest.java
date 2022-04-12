/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoPointLinearEquationTest extends AbstractLinearEquationTest {

    @Override
    protected LinearEquation createBlueLinearEquation() {
        return new TwoPointLinearEquation(0, 2.5, 2.5, 0);
    }

    @Override
    protected LinearEquation createRedLinearEquation() {
        return new TwoPointLinearEquation(-2, 0, 0, 1);
    }

    @Test
    public void onePoint() {
        LinearEquation linearEquation = new TwoPointLinearEquation(1, 1);
        double fX = linearEquation.fX(0.5);
        assertEquals(0.5, fX, 0.00001);
    }
}
