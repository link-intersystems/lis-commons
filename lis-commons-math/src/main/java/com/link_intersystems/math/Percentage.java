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

import com.link_intersystems.lang.Assert;

/**
 * {@link Percentage} calculates the ratio in relation to units.
 * E.g. if a {@link Percentage} was constructed with 50 max units the
 * ratio of 50 unit is "1.0". The ratio of 25 units is "0.5".
 * This means that a {@link Percentage} calculates the
 * ratio of units in relation to the max units that are assumed to "1.0" (100%).
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public class Percentage {

    private static final double PER_CENT_UNIT = 1.0;

    /**
     * 1 part in 100.
     */
    public static final double PER_CENT = 100.0;

    /**
     * 1 part in 1.000.
     */
    public static final double PER_MIL = PER_CENT_UNIT * 10;

    /**
     * 1 part in 10.000.
     */
    public static final double BASE_POINT = PER_MIL * 10;

    /**
     * 1 part in 1.0.0;.
     */
    public static final double PER_CENT_MIL = BASE_POINT * 10;

    private LinearEquation unitsToPercentageEquation;

    /**
     * Constructs a percentage .
     *
     * @param maxUnits the maximum of elements this {@link Percentage} should
     *                 calculate the {@link #ratio(int)} of.
     * @since 1.2.0;
     */
    public Percentage(int maxUnits) {
        Assert.greaterOrEqual("maxUnits", 1.0, maxUnits);
        unitsToPercentageEquation = new TwoPointLinearEquation(0.0, 0.0, maxUnits, 1.0);
    }

    /**
     * Sets the maximum of units.
     *
     * @param maxUnits
     */
    public final void setMaxUnits(int maxUnits) {
        unitsToPercentageEquation = new TwoPointLinearEquation(0.0, 0.0, maxUnits, PER_CENT_UNIT);
    }

    /**
     * Returns the ratio of the given units in relation to the max units. The result will be a value  between 0.0  and 1.0.
     *
     * @return the ratio of the given units in relation to the max units of this {@link Percentage}.
     * @since 1.2.0;
     */
    public double ratio(int units) {
        return unitsToPercentageEquation.fX(units);
    }

}
