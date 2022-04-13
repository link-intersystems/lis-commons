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

class CartesianPointTest  {

    @Test
    void toPolarPoint() {
        CartesianPoint cartesianPoint = new CartesianPoint(12, 5);
        PolarPoint polarPoint = cartesianPoint.toPolarPoint();
        double polarAxis = polarPoint.getPolarAxis();
        double distance = polarPoint.getDistance();

        assertEquals(22.6, polarAxis, 0.1);
        assertEquals(13, distance, 0.1);
    }
}
