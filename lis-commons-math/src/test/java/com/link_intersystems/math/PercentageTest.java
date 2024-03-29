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
import static org.junit.jupiter.api.Assertions.assertThrows;

class PercentageTest  {

    @Test
    void wrongMax() {
        assertThrows(IllegalArgumentException.class, () -> new Percentage(0));
    }

    @Test
    void percent() {
        Percentage percentage = new Percentage(50);
        double expected = 0.0;
        for (int i = 0; i <= 100; i++) {
            double result = percentage.ratio(i);
            assertEquals(expected, result, 0.00001);
            expected += 0.02;
        }
    }

    @Test
    void resetMaxUnits() {
        Percentage percentage = new Percentage(50);
        double result = percentage.ratio(10);
        assertEquals(0.2, result, 0.00001);

        percentage.setMaxUnits(100);
        result = percentage.ratio(10);
        assertEquals(0.1, result, 0.00001);
    }

}
