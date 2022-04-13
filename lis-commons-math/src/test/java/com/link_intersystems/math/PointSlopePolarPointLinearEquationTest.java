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

class PointSlopePolarPointLinearEquationTest extends AbstractLinearEquationTest  {

    @Override
    protected LinearEquation createBlueLinearEquation() {
        return new PointSlopeLinearEquation(-1.0, new PolarPoint(2.5, 90.0));
    }

    @Override
    protected LinearEquation createRedLinearEquation() {
        return new PointSlopeLinearEquation(0.5, new PolarPoint(2.0, 180.0));
    }

    @Test
    void doubleSlopeOnly() {
        LinearEquation linearEquation = new PointSlopeLinearEquation(1.5);
        double fX = linearEquation.fX(1);
        assertEquals(1.5, fX, 0.001);

        double fY = linearEquation.fY(1.5);
        assertEquals(1.0, fY, 0.001);
    }

    @Test
    void slopeOnly() {
        Slope slope = new Slope(1.5);
        LinearEquation linearEquation = new PointSlopeLinearEquation(slope);
        double fX = linearEquation.fX(1);
        assertEquals(1.5, fX, 0.001);

        double fY = linearEquation.fY(1.5);
        assertEquals(1.0, fY, 0.001);
    }

    @Test
    void slopeWithB() {
        Slope slope = new Slope(1.5);
        LinearEquation linearEquation = new PointSlopeLinearEquation(slope, 1);
        double fX = linearEquation.fX(1);
        assertEquals(2.5, fX, 0.001);

        double fY = linearEquation.fY(2.5);
        assertEquals(1.0, fY, 0.001);
    }

    @Test
    void slopeWithXandY() {
        LinearEquation linearEquation = new PointSlopeLinearEquation(1.5, 1, 1.5);
        double fX = linearEquation.fX(1);
        assertEquals(1.5, fX, 0.001);

        double fY = linearEquation.fY(1.5);
        assertEquals(1.0, fY, 0.001);
    }

    @Test
    void dobleSlopeWithB() {
        LinearEquation linearEquation = new PointSlopeLinearEquation(1.5, 1.0);
        double fX = linearEquation.fX(1);
        assertEquals(2.5, fX, 0.001);

        double fY = linearEquation.fY(2.5);
        assertEquals(1.0, fY, 0.001);
    }

    @Test
    void doubleSlopeWithCartesianPoint() {
        LinearEquation linearEquation = new PointSlopeLinearEquation(1.5, new CartesianPoint(0, 1.0));
        double fX = linearEquation.fX(1);
        assertEquals(2.5, fX, 0.001);

        double fY = linearEquation.fY(2.5);
        assertEquals(1.0, fY, 0.001);
    }
}
