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

class BigDecimalSumTest  {

    @Test
    void sumNoValues() {
        Sum<BigDecimal> sum = new BigDecimalSum();
        BigDecimal value = sum.getValue();
        assertEquals(0, value.doubleValue(), 0.000001);
    }

    @Test
    void sum() {
        Sum<BigDecimal> sum = new BigDecimalSum();
        boolean changed = sum.addValue(5);
        assertTrue(changed);
        changed = sum.addValue(5);
        assertTrue(changed);
        changed = sum.addValue(2.3);
        assertTrue(changed);
        sum.addValue(9.2);
        sum.addValue(1.04);
        BigDecimal value = sum.getValue();
        assertEquals(22.54, value.doubleValue(), 0.000001);
    }

    @Test
    void add0() {
        Sum<BigDecimal> sum = new BigDecimalSum();
        assertFalse(sum.addValue(BigDecimal.ZERO));
    }
}
