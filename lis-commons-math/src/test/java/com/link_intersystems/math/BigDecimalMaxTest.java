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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BigDecimalMaxTest {

    @Test
    public void maxNoValues() {
        Max<BigDecimal> max = new BigDecimalMax();
        BigDecimal value = max.getValue();
        assertNull(value);
    }

    @Test
    public void max() {
        Max<BigDecimal> max = new BigDecimalMax();
        boolean valueChanged = max.addValue(5);
        assertTrue(valueChanged);
        valueChanged = max.addValue(5);
        assertFalse(valueChanged);
        max.addValue(2);
        max.addValue(9);
        max.addValue(1);
        max.addValue(-32);
        max.addValue(53.2);
        max.addValue(53.6);
        BigDecimal value = max.getValue();
        assertEquals(53.6, value.doubleValue(), 0.000001);
    }
}
